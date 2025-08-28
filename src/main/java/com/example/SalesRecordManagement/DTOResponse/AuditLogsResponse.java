package com.example.SalesRecordManagement.DTOResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class AuditLogsResponse {
    private UUID LogId;
    private String action;
    private LocalDateTime timestamp;
    private String username;
}
