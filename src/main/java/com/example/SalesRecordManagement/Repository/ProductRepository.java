package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.DTOResponse.CompanyResponse;
import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCodeAndCompany(String code, Company company);
    List<Product> findByCompany(Company company);


}
