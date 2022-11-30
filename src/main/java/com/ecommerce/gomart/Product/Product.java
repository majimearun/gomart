package com.ecommerce.gomart.Product;

import com.ecommerce.gomart.Cart.Cart;
import com.ecommerce.gomart.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    // one to many relationship with cart
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts;

    // one to many relationship with orders
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;



}
