package com.enigma.enigma_shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCustomerRequest {
    private String name;
    private String mobilePhoneNumber;
    private String birthDate;
    private Boolean status;
    private Integer page;
    private Integer size;
}
