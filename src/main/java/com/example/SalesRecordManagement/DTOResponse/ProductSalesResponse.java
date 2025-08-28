package com.example.SalesRecordManagement.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductSalesResponse {
    private UUID productId;
    private String productName;
    private String category;
    private Long totalSale;
}
