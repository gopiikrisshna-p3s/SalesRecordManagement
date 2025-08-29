package com.example.SalesRecordManagement.DTOResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class RecentSaleResponse {
    private UUID saleId;
    private String productName;
    private String companyName;
    private int quantity;
    private double revenue;
    private LocalDate saleDate;

    public RecentSaleResponse(UUID saleId, String productName, String companyName,
                              int quantity, double revenue, LocalDate saleDate) {
        this.saleId = saleId;
        this.productName = productName;
        this.companyName = companyName;
        this.quantity = quantity;
        this.revenue = revenue;
        this.saleDate = saleDate;
    }
}
