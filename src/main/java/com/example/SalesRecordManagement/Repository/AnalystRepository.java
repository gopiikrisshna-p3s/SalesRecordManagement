package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Analyst;
import com.example.SalesRecordManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnalystRepository extends JpaRepository<Analyst, UUID> {
    Analyst findByUser(Users users);
}
