package com.example.SalesRecordManagement.DTOResponse;

import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerProfileResponse {
    private Long customerId;
    private String customerName;
    private String email;
    private List<CompanyResponse> companyList;
}
