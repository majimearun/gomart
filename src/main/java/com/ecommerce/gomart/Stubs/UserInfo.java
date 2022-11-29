package com.ecommerce.gomart.controllers;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo {
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String address;
    private String phone;
    
}
