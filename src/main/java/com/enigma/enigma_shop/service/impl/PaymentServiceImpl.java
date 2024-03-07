package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.PaymentDetailRequest;
import com.enigma.enigma_shop.dto.request.PaymentItemDetailRequest;
import com.enigma.enigma_shop.dto.request.PaymentRequest;
import com.enigma.enigma_shop.dto.response.PaymentResponse;
import com.enigma.enigma_shop.entity.Payment;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.entity.TransactionDetail;
import com.enigma.enigma_shop.repository.PaymentRepository;
import com.enigma.enigma_shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;
    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RestClient restClient,
            @Value("U0ItTWlkLXNlcnZlci10OUtYYTlPYW9yaWRvWWt0ektPX25JRDg=") String secretKey,
            @Value("https://app.sandbox.midtrans.com/snap/v1/transactions") String baseUrlSnap
    ) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretKey;
        BASE_URL_SNAP = baseUrlSnap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Transaction transaction) {
        long amount = transaction.getTransactionDetails()
                .stream().mapToLong(value -> (value.getQty()) * value.getProductPrice())
                .reduce(0, Long::sum);

        List<PaymentItemDetailRequest> itemDetailRequests = transaction.getTransactionDetails().stream()
                .map(transactionDetail -> PaymentItemDetailRequest.builder()
                        .name(transactionDetail.getProduct().getName())
                        .price(transactionDetail.getProductPrice())
                        .quantity(transactionDetail.getQty())
                        .build()).toList();

        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(transaction.getId())
                        .amount(amount)
                        .build())
                .paymentItemDetails(itemDetailRequests)
                .paymentMethod(List.of("shopeepay","gopay"))
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(BASE_URL_SNAP)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String ,String> body = response.getBody();

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus("ordered")
                .build();
        paymentRepository.saveAndFlush(payment);

        return payment;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdatePayments() {
        List<String> transactionStatus = List.of("deny", "cancel", "expire", "failure");
        List<Payment> payments = paymentRepository.findAllByTransactionStatusIn(transactionStatus);

        for (Payment payment : payments) {
            for (TransactionDetail transactionDetail : payment.getTransaction().getTransactionDetails()) {
                Product product = transactionDetail.getProduct();
                product.setStock(transactionDetail.getProduct().getStock() + transactionDetail.getQty());
            }
            payment.setTransactionStatus("stock_returned");
        }
    }
}
