package com.ecommerce.gomart.Stubs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Login {
    private String email;
    private String password;
    Long userId;

}
