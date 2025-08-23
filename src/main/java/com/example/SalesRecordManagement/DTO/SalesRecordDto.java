package com.example.SalesRecordManagement.DTO;

import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Product;
import com.example.SalesRecordManagement.Entity.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesRecordDto {
    private CustomerDto customer;
    private CompanyDto company;
    private ProductDto products;
    private SaleDto sales;
}
