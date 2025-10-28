package com.bank.rest.repositories;

import com.bank.rest.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.rest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, Long> {

    Page<Transfer> findByUser(User user, Pageable pageable);

    Page<Transfer> findByUserAndStatus(User user, String status, Pageable pageable);
}


