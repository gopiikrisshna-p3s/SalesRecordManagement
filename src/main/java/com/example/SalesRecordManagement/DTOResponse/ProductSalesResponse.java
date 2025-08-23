package com.example.SalesRecordManagement.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
public class ProductSalesResponse {
    private Long productId;
    private String productName;
    private String category;
    private Long totalSale;
}
