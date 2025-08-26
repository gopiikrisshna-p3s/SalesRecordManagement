package com.example.SalesRecordManagement.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProfileDto {
    private String name;
    private String email;
    private String phone;
}
