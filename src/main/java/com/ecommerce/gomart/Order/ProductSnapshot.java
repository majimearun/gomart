package com.ecommerce.gomart.Order;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import com.ecommerce.gomart.Product.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductSnapshot{
    @Column(
            name = "product_name",
            nullable = false
    )
    private String name;

    @Column(
            name = "product_price",
            nullable = false
    )
    private double price;

    @Column(
            name = "product_image"
    )
    @Lob
    private byte[] image;

    @Column(
                name = "offer",
                nullable = false
    )
    private Double offer;

    @Column(
        name = "delivery_time",
        nullable = false
    )
    private Integer deliveryTime;

}

