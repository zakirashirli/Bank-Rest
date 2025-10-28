package com.bank.rest.controller;

import com.bank.rest.entity.User;
import com.bank.rest.dto.TransferRequest;
import com.bank.rest.dto.TransferResponse;
import com.bank.rest.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;


    /**
     * Perform a money transfer between user's own cards.
     */
    @PostMapping
    public TransferResponse makeTransfer(
            @AuthenticationPrincipal User user,
            @RequestBody TransferRequest request
    ) {
        return transferService.transfer(user, request);
    }

    /**
     * Get all transfers performed by the current user (paginated).
     */
    @GetMapping
    public Page<TransferResponse> getUserTransfers(
            @AuthenticationPrincipal
            User user,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {
        return transferService.getUserTransfers(user, PageRequest.of(page, size));
    }

}

