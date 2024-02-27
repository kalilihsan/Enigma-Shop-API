package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.entity.TransactionDetail;
import com.enigma.enigma_shop.repository.TransactionRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.service.ProductService;
import com.enigma.enigma_shop.service.TransactionDetailService;
import com.enigma.enigma_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Transaction create(TransactionRequest request) {
        // cari/validasi customer
        Customer customer = customerService.getById(request.getCustomerId());

        // 1. Save Transaction Table
        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        transactionRepository.saveAndFlush(trx);

        // 2. Save Transaction Detail Table
        List<TransactionDetail> trxDetails = request.getTransactionDetails().stream()
                .map(detailRequest -> {
                    Product product = productService.getById(detailRequest.getProductId());

                    if (product.getStock() - detailRequest.getQty() < 0) throw new RuntimeException("Sold out");

                    product.setStock(product.getStock() - detailRequest.getQty());

                    return TransactionDetail.builder()
                            .product(product)
                            .transaction(trx)
                            .qty(detailRequest.getQty())
                            .productPrice(product.getPrice())
                            .build();
                }).toList();

        trx.setTransactionDetails(trxDetails);
        transactionDetailService.createBulk(trxDetails);
        return trx;
    }
}
