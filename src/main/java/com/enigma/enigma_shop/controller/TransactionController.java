package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.dto.response.TransactionResponse;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponse createNewTransaction(@RequestBody TransactionRequest request) {
        return transactionService.create(request);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransaction() {
        return transactionService.getAll();
    }

}
