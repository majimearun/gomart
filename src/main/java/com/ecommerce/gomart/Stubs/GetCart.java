package com.ecommerce.gomart.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class GetCart {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
