package com.ecommerce.gomart.stub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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