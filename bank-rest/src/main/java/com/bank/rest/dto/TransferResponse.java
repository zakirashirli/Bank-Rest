package com.bank.rest.dto;

public record TransferResponse(
        long id,
        String status,
        String message
) {}

