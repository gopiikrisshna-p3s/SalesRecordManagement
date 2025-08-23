package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTOResponse.*;
import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Product;
import com.example.SalesRecordManagement.Repository.CompanyRepository;
import com.example.SalesRecordManagement.Repository.ProductRepository;
import com.example.SalesRecordManagement.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SaleRepository saleRepository;

    public CustomerProfileResponse getCustomerProfile(Customer customer) {
        List<CompanyResponse> companies = companyRepository.findAllByCustomer(customer)
                .stream()
                .map(company -> {
                    List<ProductResponse> products = productRepository.findByCompany(company)
                            .stream()
                            .map(product -> new ProductResponse(
                                    product.getProductId(),
                                    product.getProductName(),
                                    product.getCategory()
                            ))
                            .toList();
                    return new CompanyResponse(
                            company.getCompanyId(),
                            company.getCompanyName(),
                            company.getCompanyAddress(),
                            products
                    );
                })
                .toList();

        return CustomerProfileResponse.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .email(customer.getEmail())
                .companyList(companies)
                .build();
    }

    public List<ProductSalesResponse> getTopProductsByCompany(Long companyId) {
        List<Object[]> results = saleRepository.findTopProductsByCompany(companyId);

        return results.stream()
                .map(prod -> new ProductSalesResponse(
                        ((Product) prod[0]).getProductId(),
                        ((Product) prod[0]).getProductName(),
                        ((Product) prod[0]).getCategory(),
                        (Long) prod[1]
                ))
                .toList();
    }

    public List<ProductSalesReportResponse> getSalesReportByProduct(Long productId) {
        List<Object[]> results = saleRepository.getSalesReportByProduct(productId);

        return results.stream().map(obj -> ProductSalesReportResponse.builder()
                .productId((Long) obj[0])
                .productName((String) obj[1])
                .category((String) obj[2])
                .date((LocalDate) obj[3])
                .quantity(((Number) obj[4]).intValue())
                .price(((Number) obj[5]).doubleValue())
                .revenue(((Number) obj[6]).doubleValue())
                .cost(((Number) obj[7]).doubleValue())
                .profit(((Number) obj[8]).doubleValue())
                .region((String) obj[9])
                .build()
        ).toList();
    }
    public List<ProductSalesReportResponse> getRecentSalesByProduct(Long productId, int limit) {
        List<Object[]> results = saleRepository.getRecentSalesByProduct(productId);

        return results.stream()
                .limit(limit)
                .map(obj -> ProductSalesReportResponse.builder()
                        .productId((Long) obj[0])
                        .productName((String) obj[1])
                        .category((String) obj[2])
                        .date((LocalDate) obj[3])
                        .quantity((Integer) obj[4])
                        .price((Double) obj[5])
                        .revenue((Double) obj[6])
                        .cost((Double) obj[7])
                        .profit((Double) obj[8])
                        .region((String) obj[9])
                        .build()
                )
                .toList();
    }
}
