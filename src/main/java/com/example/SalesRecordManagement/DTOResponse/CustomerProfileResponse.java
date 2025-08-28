package com.example.SalesRecordManagement.DTOResponse;

import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomerProfileResponse {
    private UUID customerId;
    private String customerName;
    private String email;
    private List<CompanyResponse> companyList;
}
