package com.ecommerce.gomart.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
        name = "gomart_products"
)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    @Column(
            name = "product_id",
            nullable = false
    )
    private Long productId;

    @Column(
            name = "product_name",
            nullable = false
    )
    private String name;

    @Column(
            name = "product_description"
    )
    private String description;

    @Column(
            name = "product_price",
            nullable = false
    )
    private double price;

    @Column(
            name = "product_quantity",
            nullable = false
    )
    private Integer quantity;

    @Column(
            name = "product_image"
    )
    private String image;

    @Column(
            name = "product_category",
            nullable = false
    )
    private Category category;

    @Column(
            name = "offer"
    )
    private Double offer;

}
