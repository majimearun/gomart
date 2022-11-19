package com.ecommerce.gomart.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
@Table(
        name = "gomart_cart_items"
)

public class Cart {
    @Id
    @SequenceGenerator(
            name="cart_sequence",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_sequence")
    @Column(
            name = "entry_id"
    )
    private Long entryId;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;


    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    @Column(
            name = "quantity",
            nullable = false
    )
    private Integer quantity;

}
