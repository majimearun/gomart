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
            name="entry_sequence",
            sequenceName = "entry_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_sequence")
    @Column(
            name = "entry",
            nullable = false,
            updatable = false
    )
    private Long entry;

    @Column(
            name = "quantity",
            nullable = false
    )
    private Integer quantity;

    @OneToOne
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;


}
