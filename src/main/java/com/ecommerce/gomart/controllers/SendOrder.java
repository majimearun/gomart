package com.ecommerce.gomart.controllers;

import java.time.LocalDate;

import com.ecommerce.gomart.models.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SendOrder {
    private Long transactionId;
    private Product product;
    private int quantity;
    private LocalDate orderDate;
    
}
