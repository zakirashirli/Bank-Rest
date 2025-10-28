package com.bank.rest.entity;

import com.bank.rest.domainRoles.CardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cardID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(name="number_enc", nullable=false)
    private String numberEnc;
    @Column(name="number_last4", nullable=false, length=4)
    private String numberLast4;

    private String holder;
    private int expiryMonth;
    private int expiryYear;

    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.ACTIVE;
    @Column(name="balance_minor", nullable=false)
    private long balanceMinor;
    @Column(length=3)
    private String currency = "RUB";

    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    // To view the transactions to specific card
    @OneToMany(mappedBy = "fromCard", cascade = CascadeType.ALL)
    private java.util.List<Transfer> outgoingTransfers;

    @OneToMany(mappedBy = "toCard", cascade = CascadeType.ALL)
    private java.util.List<Transfer> incomingTransfers;

}

