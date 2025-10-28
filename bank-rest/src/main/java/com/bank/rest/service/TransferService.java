package com.bank.rest.service;

import com.bank.rest.domainRoles.CardStatus;
import com.bank.rest.entity.*;
import com.bank.rest.dto.TransferRequest;
import com.bank.rest.dto.TransferResponse;
import com.bank.rest.exception.AppException;
import com.bank.rest.repositories.CardRepo;
import com.bank.rest.repositories.TransferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {
    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private TransferRepo transferRepo;


    @Transactional
    public TransferResponse transfer(User user, TransferRequest req) {
        var transfer = new Transfer();

        try {
            if (req.fromCardId().equals(req.toCardId())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Cannot transfer to the same card");
            }

            var fromCard = cardRepo.lockById(req.fromCardId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Source card not found"));

            var toCard = cardRepo.lockById(req.toCardId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Destination card not found"));

            if (!fromCard.getUser().getId().equals(user.getId()) ||
                    !toCard.getUser().getId().equals(user.getId())) {
                throw new AppException(HttpStatus.FORBIDDEN, "You can only transfer between your own cards");
            }

            if (fromCard.getStatus() != CardStatus.ACTIVE || toCard.getStatus() != CardStatus.ACTIVE) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Both cards must be active");
            }

            if (!fromCard.getCurrency().equals(toCard.getCurrency())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Cards must use the same currency");
            }

            if (fromCard.getBalanceMinor() < req.amountMinor()) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }

            fromCard.setBalanceMinor(fromCard.getBalanceMinor() - req.amountMinor());
            toCard.setBalanceMinor(toCard.getBalanceMinor() + req.amountMinor());
            cardRepo.save(fromCard);
            cardRepo.save(toCard);

            transfer = new Transfer(user, fromCard, toCard, req.amountMinor(), fromCard.getCurrency());
            transfer.setStatus("SUCCESS");
            transfer.setMessage("Transfer completed successfully");
            transferRepo.save(transfer);

            return new TransferResponse(transfer.getId(), transfer.getStatus(), transfer.getMessage());
        } catch (Exception e) {
            transfer.setStatus("FAILED");
            transfer.setMessage("Transfer failed: " + e.getMessage());
            transferRepo.save(transfer);
            throw e;
        }
    }

    public Page<TransferResponse> getUserTransfers(User user, Pageable pageable) {
        var page = transferRepo.findByUser(user, pageable);
        return page.map(t -> new TransferResponse(
                t.getId(),
                t.getStatus(),
                t.getMessage()
        ));
    }
}

