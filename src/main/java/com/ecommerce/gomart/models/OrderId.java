package com.ecommerce.gomart.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class OrderId implements Serializable {
    private Long orderTrackingId;

    private Long productId;

}
