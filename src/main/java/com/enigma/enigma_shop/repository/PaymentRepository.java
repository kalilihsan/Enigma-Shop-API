package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String > {
    List<Payment> findAllByTransactionStatusIn(List<String> transactionStatus);
}
