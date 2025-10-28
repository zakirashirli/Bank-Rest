package com.bank.rest.service;

import com.bank.rest.domainRoles.CardStatus;
import com.bank.rest.domainRoles.UserRole;
import com.bank.rest.dto.CardCreateRequest;
import com.bank.rest.dto.CardFilter;
import com.bank.rest.dto.CardResponse;
import com.bank.rest.entity.Card;
import com.bank.rest.entity.User;
import com.bank.rest.exception.AppException;
import com.bank.rest.mapper.CardMapper;
import com.bank.rest.repositories.CardRepo;
import com.bank.rest.repositories.UserRepo;
import com.bank.rest.security.CryptoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepo cardRepo;
    private final UserRepo userRepo;
    private final CryptoService cryptoService;

    public CardService(CardRepo cardRepo, UserRepo userRepo, CryptoService cryptoService) {
        this.cardRepo = cardRepo;
        this.userRepo = userRepo;
        this.cryptoService = cryptoService;
    }


     // Create a new card for the user.

    @Transactional
    public CardResponse createCard(Long userId, CardCreateRequest req) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        var number = req.number().replaceAll("\\s+", "");
        if (number.length() < 12 || number.length() > 19) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid card number format");
        }

        var encrypted = cryptoService.encrypt(number);

        var card = new Card();
        card.setUser(user);
        card.setNumberEnc(encrypted);
        card.setNumberLast4(number.substring(number.length() - 4));
        card.setHolder(req.holder());
        card.setExpiryMonth(req.expiryMonth());
        card.setExpiryYear(req.expiryYear());
        card.setCurrency(req.currency() != null ? req.currency() : "AZN");
        card.setBalanceMinor(req.initialBalanceMinor());
        card.setStatus(deriveStatus(req.expiryMonth(), req.expiryYear()));

        cardRepo.save(card);
        return CardMapper.toResponse(card);
    }

    // Derive card status from expiry date.

    private CardStatus deriveStatus(int month, int year) {
        return YearMonth.of(year, month).isBefore(YearMonth.now())
                ? CardStatus.EXPIRED
                : CardStatus.ACTIVE;
    }

    // Get all cards for a specific user with optional filters.

    public Page<CardResponse> getUserCards(User user, CardFilter filter, Pageable pageable) {
        Page<Card> cards;
        if (filter != null && filter.status() != null) {
            cards = cardRepo.findByUserAndStatusInAndNumberLast4ContainingIgnoreCase(
                    user,
                    List.of(filter.status()),
                    filter.query() != null ? filter.query() : "",
                    pageable
            );
        } else {
            cards = cardRepo.findByUser(user, pageable);
        }
        return cards.map(CardMapper::toResponse);
    }

    /**
     * Block a card (User or Admin)
     */
    @Transactional
    public void blockCard(UUID cardId, User user) {
        var card = cardRepo.findById(cardId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Card not found"));

        if (!card.getUser().getId().equals(user.getId())
                && user.getRole() != UserRole.ADMIN) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only block your own cards");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepo.save(card);
    }

    /**
     * Activate or unblock a card (Admin only)
     */
    @Transactional
    public void activateCard(UUID cardId) {
        var card = cardRepo.findById(cardId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Card not found"));
        card.setStatus(CardStatus.ACTIVE);
        cardRepo.save(card);
    }

    /**
     * Delete card (Admin only)
     */
    @Transactional
    public void deleteCard(UUID cardId) {
        if (!cardRepo.existsById(cardId)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Card not found");
        }
        cardRepo.deleteById(cardId);
    }
}
