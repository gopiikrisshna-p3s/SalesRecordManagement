package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogsRepository extends JpaRepository<AuditLogs,Long> {
}
