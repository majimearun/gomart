package com.ecommerce.gomart.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.persistence.*;

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
        @Lob
        private byte[] image;

        @Column(
                name = "product_category",
                nullable = false
        )
        private Category category;

        @Column(
                name = "offer"
        )
        private Double offer;

        @Column(
                name = "delivery_time",
                nullable = false
        )
        // in days
        private Integer deliveryTime;

        @OneToMany(
                        mappedBy = "product",
                        cascade = CascadeType.ALL,
                        orphanRemoval = true
        )
        private List<Cart> cartItems;

        @OneToMany(
                        mappedBy = "product",
                        cascade = CascadeType.ALL,
                        orphanRemoval = true
        )
        private List<Order> orders;

}
