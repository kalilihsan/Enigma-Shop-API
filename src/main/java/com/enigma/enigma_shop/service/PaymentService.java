package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.response.PaymentResponse;
import com.enigma.enigma_shop.entity.Payment;
import com.enigma.enigma_shop.entity.Transaction;

public interface PaymentService {
    Payment createPayment(Transaction transaction);

    void checkFailedAndUpdatePayments();
}
