package com.enigma.enigma_shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String username;
    private String password;
}
