package com.bank.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    @Column(name = "amount_minor", nullable = false)
    private long amountMinor;

    @Column(length = 3, nullable = false)
    private String currency = "RUB";

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(length = 255)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Transfer(User user, Card fromCard, Card toCard, long amountMinor, String currency) {
        this.user = user;
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.amountMinor = amountMinor;
        this.currency = currency;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }
}

