package com.ecommerce.gomart.Order;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerSnapshot {

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
        name = "first_name",
        nullable = false
)
    private String firstName;

    @Column(
        name = "last_name",
        nullable = false
)
    private String lastName;
    
    @Column(
                name = "address",
                nullable = false
        )
    private String address;

    @Column(
                name = "user_id",
                nullable = false,
                updatable = false
        )
    private Long userId;
}
