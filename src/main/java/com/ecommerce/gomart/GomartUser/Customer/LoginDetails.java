package com.ecommerce.gomart.GomartUser.Customer;

import com.ecommerce.gomart.GomartUser.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginDetails {
    private Long userId;
    private Role role;

}
