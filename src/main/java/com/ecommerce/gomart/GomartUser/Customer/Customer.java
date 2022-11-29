package com.ecommerce.gomart.GomartUser.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer{
    @Embedded
    private Wallet wallet;

}
