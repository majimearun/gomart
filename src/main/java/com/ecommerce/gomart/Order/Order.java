package com.ecommerce.gomart.Order;


import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "gomart_orders"
)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Order {
    @Id
    @SequenceGenerator(
            name="order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(
            name = "transaction_id",
            nullable = false
    )
    private Long orderTransactionId;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;


    @Column(
            name = "quantity",
            nullable = false
    )
    private Integer quantity;

    @Column(
            name = "order_date"
    )
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private GomartUser customer;

}
