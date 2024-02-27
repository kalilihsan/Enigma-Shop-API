package com.enigma.enigma_shop.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String customerId;
    private Date transDate;
    private List<TransactionDetailResponse> transactionDetails;
}
