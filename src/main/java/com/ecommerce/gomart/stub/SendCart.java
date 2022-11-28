package com.ecommerce.gomart.stub;

import com.ecommerce.gomart.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SendCart {
    private Product product;
    private int quantity;
}
