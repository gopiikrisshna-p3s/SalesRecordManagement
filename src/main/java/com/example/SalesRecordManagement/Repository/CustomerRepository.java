package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByUser(Users users);
}
