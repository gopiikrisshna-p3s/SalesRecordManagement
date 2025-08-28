package com.example.SalesRecordManagement.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfitResponse {
    private UUID companyId;
    private String companyName;
    private Double totalProfit;
}
