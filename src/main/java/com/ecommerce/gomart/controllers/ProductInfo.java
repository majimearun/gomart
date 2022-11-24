package com.ecommerce.gomart.controllers;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.gomart.models.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProductInfo {
    private Long userId;
    private Product product;
    
}
