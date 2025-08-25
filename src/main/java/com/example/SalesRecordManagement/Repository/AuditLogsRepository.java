package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.DTOResponse.AuditLogsResponse;
import com.example.SalesRecordManagement.Entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogsRepository extends JpaRepository<AuditLogs,Long> {
    @Query("SELECT new com.example.SalesRecordManagement.DTOResponse.AuditLogsResponse(" +
            "a.logId, a.action, a.timestamp, a.user.username) " +
            "FROM AuditLogs a " +
            "WHERE a.timestamp BETWEEN :startDate AND :endDate")
    List<AuditLogsResponse> findLogsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
