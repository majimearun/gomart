package com.ecommerce.gomart.Stubs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class GetCart {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
