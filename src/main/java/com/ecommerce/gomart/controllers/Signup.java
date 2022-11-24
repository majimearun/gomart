package com.ecommerce.gomart.controllers;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Signup {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private double amount;
    
}
