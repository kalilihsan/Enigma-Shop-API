package com.enigma.enigma_shop.dto.response;

import com.enigma.enigma_shop.entity.Image;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private Long price;
    private Integer stock;
    private ImageResponse image;
}
