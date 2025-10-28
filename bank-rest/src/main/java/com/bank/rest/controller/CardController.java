package com.bank.rest.controller;

import com.bank.rest.entity.User;
import com.bank.rest.dto.*;
import com.bank.rest.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public CardResponse createCard(
            @AuthenticationPrincipal User user,
            @RequestBody CardCreateRequest req
    ) {
        return cardService.createCard(user.getId(), req);
    }

    @GetMapping
    public Page<CardResponse> getMyCards(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status
    ) {
        CardFilter filter = new CardFilter(
                query,
                status != null ? Enum.valueOf(com.bank.rest.domainRoles.CardStatus.class, status) : null,
                null
        );
        return cardService.getUserCards(user, filter, PageRequest.of(page, size));
    }

    @PatchMapping("/{cardId}/block")
    public void blockCard(
            @AuthenticationPrincipal User user,
            @PathVariable UUID cardId
    ) {
        cardService.blockCard(cardId, user);
    }

    @PatchMapping("/{cardId}/activate")
    public void activateCard(@PathVariable UUID cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCard(@PathVariable UUID cardId) {
        cardService.deleteCard(cardId);
    }
}

