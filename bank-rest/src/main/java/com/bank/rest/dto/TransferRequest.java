package com.bank.rest.dto;

import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record TransferRequest(
        UUID fromCardId,
        UUID toCardId,
        @Positive long amountMinor
) {}

