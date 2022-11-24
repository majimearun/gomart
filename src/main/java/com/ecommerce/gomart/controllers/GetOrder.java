package com.ecommerce.gomart.controllers;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class GetOrder {
    private Long senderId;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
}
