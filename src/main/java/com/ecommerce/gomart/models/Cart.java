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
@IdClass(CartId.class)
public class Cart {
    @Id
    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(
            name = "quantity",
            nullable = false
    )
    private Integer quantity;



}
