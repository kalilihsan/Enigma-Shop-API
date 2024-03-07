package com.enigma.enigma_shop.scheduler;

import com.enigma.enigma_shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusScheduler {
    private final PaymentService paymentService;
    @Scheduled(fixedRate = 60000)
    public void checkFailedPayments() {
        log.info("START checkFailedPayment() : {}", System.currentTimeMillis());
        paymentService.checkFailedAndUpdatePayments();
        log.info("END checkFailedPayment() : {}", System.currentTimeMillis());
    }
}
