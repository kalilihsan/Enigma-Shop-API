//package com.enigma.enigma_shop.service.impl;
//
//import com.enigma.enigma_shop.constant.ResponseMessage;
//import com.enigma.enigma_shop.dto.request.TransactionDetailRequest;
//import com.enigma.enigma_shop.dto.request.TransactionRequest;
//import com.enigma.enigma_shop.dto.response.TransactionDetailResponse;
//import com.enigma.enigma_shop.dto.response.TransactionResponse;
//import com.enigma.enigma_shop.entity.*;
//import com.enigma.enigma_shop.repository.CustomerRepository;
//import com.enigma.enigma_shop.repository.ProductRepository;
//import com.enigma.enigma_shop.repository.TransactionDetailRepository;
//import com.enigma.enigma_shop.repository.TransactionRepository;
//import com.enigma.enigma_shop.service.CustomerService;
//import com.enigma.enigma_shop.service.ProductService;
//import com.enigma.enigma_shop.service.TransactionDetailService;
//import com.enigma.enigma_shop.service.TransactionService;
//import com.enigma.enigma_shop.util.ValidationUtil;
//import jakarta.validation.Validator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.parameters.P;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class TransactionServiceImplTest {
//    @Mock
//    private TransactionRepository transactionRepository;
//    @Mock
//    private TransactionService transactionService;
//    @Mock
//    private TransactionDetailService transactionDetailService;
//    @Mock
//    private CustomerService customerService;
//    @Mock
//    private ProductService productService;
//    @BeforeEach
//    void setUp() {
//        transactionService = new TransactionServiceImpl(transactionRepository, transactionDetailService
//                , customerService, productService);
//    }
//
//    @Test
//    void shouldReturnTransactionResponseWhenCreate() {
//        TransactionRequest request = TransactionRequest.builder()
//                .customerId("Customer-1")
//                .transactionDetails(List.of(
//                        TransactionDetailRequest.builder()
//                                .productId("Product-1")
//                                .qty(2)
//                                .build(),
//                        TransactionDetailRequest.builder()
//                                .productId("Product-2")
//                                .qty(3)
//                                .build()
//                )).build();
//
//        Customer mockCustomer = Customer.builder().id(request.getCustomerId()).build();
//
//        Mockito.when(customerService.getById(request.getCustomerId())).thenReturn(mockCustomer);
//
//        Transaction trx = Transaction.builder()
//                .id("Trx-id")
//                .customer(mockCustomer)
//                .transDate(new Date())
//                .build();
//
//        Mockito.when(transactionRepository.saveAndFlush(Mockito.any())).thenReturn(trx);
//
//        List<TransactionDetail> mockTransactionDetails = new ArrayList<>();
//
//        for (TransactionDetailRequest detailRequest : request.getTransactionDetails()) {
//            Product mockProduct = Product.builder()
//                    .id(detailRequest.getProductId())
//                    .price(5000L)
//                    .stock(10)
//                    .build();
//            Mockito.when(productService.getById(detailRequest.getProductId())).thenReturn(mockProduct);
//
//            mockProduct.setStock(mockProduct.getStock() - detailRequest.getQty());
//
//            Mockito.when(productService.update(mockProduct)).thenReturn(mockProduct);
//
//            int increment = 0;
//
//            TransactionDetail mockDetail = TransactionDetail.builder()
//                    .id("trx-dt-" + ++increment)
//                    .transaction(trx)
//                    .product(mockProduct)
//                    .qty(detailRequest.getQty())
//                    .productPrice(mockProduct.getPrice())
//                    .build();
//
//            mockTransactionDetails.add(mockDetail);
//        }
//
//        Mockito.when(transactionDetailService.createBulk(Mockito.any())).thenReturn(mockTransactionDetails);
//
//        trx.setTransactionDetails(mockTransactionDetails);
//
//        TransactionResponse transactionResponse = transactionService.create(request);
//
//        assertEquals(2,transactionResponse.getTransactionDetails().size());
//
//    }
//
//    @Test
//    void getAll() {
//    }
//}