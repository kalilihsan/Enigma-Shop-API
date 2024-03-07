package com.enigma.enigma_shop.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile image;
}
