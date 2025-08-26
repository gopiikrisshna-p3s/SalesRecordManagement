package com.example.SalesRecordManagement.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportRequest {
    private String name;
    private JsonNode responseData;
}