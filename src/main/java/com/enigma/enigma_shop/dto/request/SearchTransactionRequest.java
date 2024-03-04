package com.enigma.enigma_shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTransactionRequest {
    private Integer page;
    private Integer size;
}
