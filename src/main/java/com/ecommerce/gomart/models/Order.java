package com.ecommerce.gomart.models;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gomart_orders")
@IdClass(OrderId.class)
public class Order {
    @Id
    @Column(
            name = "tracking_id",
            nullable = false
    )
    private Long orderTrackingId;

    @Id
    @Column(
            name = "product_id",
            nullable = false
    )
    private Long productId;

    @Column(
            name = "date_of_order",
            nullable = false
    )
    private LocalDate date;

    @Column(
            name = "quantity",
            nullable = false
    )
    private Integer quantity;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;

}
