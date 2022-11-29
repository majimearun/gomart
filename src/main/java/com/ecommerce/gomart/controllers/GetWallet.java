package com.ecommerce.gomart.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GetWallet {
    private Long userId;
    private double amount;
}
