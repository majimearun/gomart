package com.ecommerce.gomart.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
@Table(name = "gomart_users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = {"email"})
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

}
