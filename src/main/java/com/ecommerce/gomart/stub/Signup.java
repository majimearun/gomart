package com.ecommerce.gomart.stub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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
    private String phone;
    private String address;
    
}
