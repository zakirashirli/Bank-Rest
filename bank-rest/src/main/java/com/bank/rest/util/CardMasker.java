package com.bank.rest.util;

public final class CardMasker {
    private CardMasker() {}
    public static String mask(String last4) {
        return "**** **** **** " + last4;
    }
}

