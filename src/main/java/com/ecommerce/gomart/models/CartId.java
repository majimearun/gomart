package com.ecommerce.gomart.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class CartId implements Serializable {
    private GomartUser customer;

    private Product product;

}
