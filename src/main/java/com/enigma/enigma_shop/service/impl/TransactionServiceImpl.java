package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.SearchTransactionRequest;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.dto.request.UpdateTransactionStatusRequest;
import com.enigma.enigma_shop.dto.response.PaymentResponse;
import com.enigma.enigma_shop.dto.response.TransactionDetailResponse;
import com.enigma.enigma_shop.dto.response.TransactionResponse;
import com.enigma.enigma_shop.entity.*;
import com.enigma.enigma_shop.repository.TransactionRepository;
import com.enigma.enigma_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());

        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        transactionRepository.saveAndFlush(trx);

        List<TransactionDetail> trxDetails = request.getTransactionDetails().stream().map(detailRequest -> {
            Product product = productService.getById(detailRequest.getProductId());

            if (product.getStock() - detailRequest.getQty() < 0) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ResponseMessage.ERROR_OUT_OF_STOCK);

            product.setStock(product.getStock() - detailRequest.getQty());
            productService.update(product);

            return TransactionDetail.builder()
                    .product(product)
                    .transaction(trx)
                    .qty(detailRequest.getQty())
                    .productPrice(product.getPrice())
                    .build();
        }).toList();

        transactionDetailService.createBulk(trxDetails);
        trx.setTransactionDetails(trxDetails);

        Payment payment = paymentService.createPayment(trx);
        trx.setPayment(payment);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();

        List<TransactionDetailResponse> trxDetailResponses = trxDetails.stream().map(detail ->
                TransactionDetailResponse.builder()
                .id(detail.getId())
                .productId(detail.getProduct().getId())
                .productPrice(detail.getProductPrice())
                .quantity(detail.getQty())
                .build()).toList();

        return TransactionResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .transDate(trx.getTransDate())
                .transactionDetails(trxDetailResponses)
                .paymentResponse(paymentResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return transactions.map(trx -> {
            List<TransactionDetailResponse> trxDetailResponses = trx.getTransactionDetails().stream().map(detail -> TransactionDetailResponse.builder()
                    .id(detail.getId())
                    .productId(detail.getProduct().getId())
                    .productPrice(detail.getProductPrice())
                    .quantity(detail.getQty())
                    .build()).toList();

            return TransactionResponse.builder()
                    .id(trx.getId())
                    .customerId(trx.getCustomer().getId())
                    .transDate(trx.getTransDate())
                    .transactionDetails(trxDetailResponses)
                    .build();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateTransactionStatusRequest request) {
        Transaction transaction = transactionRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND
                ));
        Payment payment = transaction.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
