package com.bank.rest.dto;

import com.bank.rest.domainRoles.CardStatus;

public record CardFilter(
        String query,
        CardStatus status,
        Integer expiryYear
) {}

