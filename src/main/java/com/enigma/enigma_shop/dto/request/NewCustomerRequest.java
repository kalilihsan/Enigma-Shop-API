package com.enigma.enigma_shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCustomerRequest {
    private String name;

    private String mobilePhoneNumber;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;
}
