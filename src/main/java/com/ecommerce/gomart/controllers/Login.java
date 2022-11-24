package com.ecommerce.gomart.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Login {
    private Long userId;
    private String password;

}
