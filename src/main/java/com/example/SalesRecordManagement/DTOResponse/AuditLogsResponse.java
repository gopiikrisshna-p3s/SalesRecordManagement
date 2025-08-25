package com.example.SalesRecordManagement.DTOResponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class AuditLogsResponse {
    private Long LogId;
    private String action;
    private LocalDateTime timestamp;
    private String username;
}
