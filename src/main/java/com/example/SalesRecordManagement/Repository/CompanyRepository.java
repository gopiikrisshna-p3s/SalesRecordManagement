package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyNameAndCustomer(String companyName, Customer customer);
    List<Company> findAllByCustomer(Customer customer);

    @Query("SELECT c FROM Company c JOIN FETCH c.products WHERE c.companyName = :companyName")
    Company findCompanyWithProducts(@Param("companyName") String companyName);
}
