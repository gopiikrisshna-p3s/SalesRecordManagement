package com.example.SalesRecordManagement.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {
    private UUID companyId;
    private String companyName;
    private String companyAddress;
    private List<ProductResponse> products;
}
