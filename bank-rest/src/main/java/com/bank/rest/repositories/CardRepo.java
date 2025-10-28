package com.bank.rest.repositories;

import com.bank.rest.domainRoles.CardStatus;
import com.bank.rest.entity.Card;
import com.bank.rest.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepo extends JpaRepository<Card, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.id = :id")
    Optional<Card> lockById(@Param("id") UUID id);

    Page<Card> findByUserAndStatusInAndNumberLast4ContainingIgnoreCase(
            User user, java.util.Collection<CardStatus> statuses, String last4, Pageable pageable);

    Page<Card> findByUser(User user, Pageable pageable);

}
