package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.entity.Transaction;

public interface TransactionService {
    Transaction create(TransactionRequest request);
}
