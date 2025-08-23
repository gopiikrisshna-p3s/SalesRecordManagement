package com.example.SalesRecordManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private String productName;
    private String category;
    private String code;
    private String action;
    private LocalDateTime timestamp;
    private LocalDate date;
    private Integer quantity;
    private Double price;
    private Double revenue;
    private Double cost;
    private Double profit;
    private String region;
}
