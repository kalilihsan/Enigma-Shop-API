package com.enigma.enigma_shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String id;
    private String name;
    private Long price;
    private Integer stock;
}
