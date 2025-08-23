package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Analyst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalystRepository extends JpaRepository<Analyst, Long> {
}
