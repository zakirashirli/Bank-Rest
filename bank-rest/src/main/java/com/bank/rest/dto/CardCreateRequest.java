package com.bank.rest.dto;

import jakarta.validation.constraints.*;

public record CardCreateRequest(
        @NotBlank String number,
        @NotBlank String holder,
        @Min(1) @Max(12) int expiryMonth,
        @Min(2024) int expiryYear,
        @PositiveOrZero long initialBalanceMinor,
        @Pattern(regexp = "^[A-Z]{3}$") String currency
) {}

