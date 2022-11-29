package com.ecommerce.gomart.GomartUser.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@AttributeOverrides(
        @AttributeOverride(
        name = "amount",
        column = @Column(name = "wallet_amount")
        )

)
public class Wallet {
    private double amount;
}
