package com.ecommerce.gomart.models;

import lombok.*;

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
