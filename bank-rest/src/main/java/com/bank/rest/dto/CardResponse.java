package com.bank.rest.dto;

import com.bank.rest.domainRoles.CardStatus;

import java.util.UUID;

public record CardResponse(
        UUID id,
        String numberMasked,
        String holder,
        int expiryMonth,
        int expiryYear,
        CardStatus status,
        long balanceMinor,
        String currency
) {}
