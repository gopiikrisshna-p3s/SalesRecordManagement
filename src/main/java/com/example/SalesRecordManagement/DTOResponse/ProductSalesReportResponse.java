package com.example.SalesRecordManagement.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductSalesReportResponse {
    private UUID productId;
    private String productName;
    private String category;
    private LocalDate date;
    private Integer quantity;
    private Double price;
    private Double revenue;
    private Double cost;
    private Double profit;
    private String region;
}
