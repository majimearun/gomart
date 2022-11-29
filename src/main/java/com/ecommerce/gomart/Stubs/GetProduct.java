package com.ecommerce.gomart.Stubs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GetProduct {
    private int category;
    private double min;
    private double max;
}
