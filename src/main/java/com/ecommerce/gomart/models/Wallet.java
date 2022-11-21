package com.ecommerce.gomart.models;

import lombok.*;

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
