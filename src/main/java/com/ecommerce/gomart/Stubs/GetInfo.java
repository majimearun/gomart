package com.ecommerce.gomart.Stubs;

import com.ecommerce.gomart.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class GetInfo {
    private Long senderId;
    private Long userId;
    private Product product;
    private String name;
    
}
