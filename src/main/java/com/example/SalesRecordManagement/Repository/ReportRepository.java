package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    Optional<Report> findByName(String name);
}
