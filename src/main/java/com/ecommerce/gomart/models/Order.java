package com.ecommerce.gomart.models;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "gomart_orders"
)
@IdClass(OrderId.class)
public class Order {
    @Id
    @Column(
            name = "transaction_id",
            nullable = false
    )
    private Long orderTransactionId;

    @Id
    @ManyToOne(
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

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
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;

}
