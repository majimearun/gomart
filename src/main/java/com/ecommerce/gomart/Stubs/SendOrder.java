package com.ecommerce.gomart.Stubs;

import com.ecommerce.gomart.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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
