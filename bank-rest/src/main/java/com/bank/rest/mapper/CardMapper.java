package com.bank.rest.mapper;

import com.bank.rest.dto.CardResponse;
import com.bank.rest.entity.Card;
import com.bank.rest.util.CardMasker;

public class CardMapper {
    public static CardResponse toResponse(Card c) {
        return new CardResponse(
                c.getId(),
                CardMasker.mask(c.getNumberLast4()),
                c.getHolder(),
                c.getExpiryMonth(),
                c.getExpiryYear(),
                c.getStatus(),
                c.getBalanceMinor(),
                c.getCurrency()
        );
    }
}