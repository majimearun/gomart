package com.ecommerce.gomart.GomartUser;


import com.ecommerce.gomart.Cart.Cart;
import com.ecommerce.gomart.GomartUser.Admin.Admin;
import com.ecommerce.gomart.GomartUser.Customer.Customer;
import com.ecommerce.gomart.GomartUser.Manager.Manager;
import com.ecommerce.gomart.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "gomart_users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = {"email"}),
        @UniqueConstraint(name = "unique_phone", columnNames = {"phone_number"})
})
public class GomartUser {
        @Id
        @SequenceGenerator(
                name="user_sequence",
                sequenceName = "user_sequence",
                allocationSize = 1
        )
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
        @Column(
                name = "user_id",
                nullable = false,
                updatable = false
        )
        private Long userId;

        @Column(
                name = "password",
                nullable = false
        )
        private String password;

        @Column(
                name = "login_status",
                nullable = false
        )
        private boolean loginStatus;

        @Column(
                name = "first_name",
                nullable = false
        )
        private String firstName;

        @Column(
                name = "middle_name"
        )
        private String middleName;

        @Column(
                name = "last_name",
                nullable = false
        )
        private String lastName;

        @Column(
                name = "dob",
                nullable = false
        )
        private LocalDate dob;

        @Column(
                name = "email",
                nullable = false
        )
        private String email;

        @Column(
                name = "phone_number",
                nullable = false
        )
        private String phoneNumber;

        @Column(
                name = "address",
                nullable = false
        )
        private String address;

        @Column(
                name = "role",
                nullable = false
        )
        @Enumerated(EnumType.STRING)
        private Role role;

        @Embedded
        private Customer customer;

        @Embedded
        private Manager manager;

        @Embedded
        private Admin admin;

        // one to many relationship with cart
        @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Cart> carts;

        // one to many relationship with orders
        @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Order> orders;


}
