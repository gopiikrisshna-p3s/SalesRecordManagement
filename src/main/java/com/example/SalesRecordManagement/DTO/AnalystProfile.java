package com.example.SalesRecordManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class AnalystProfile {
    private String analystName;
    private String analystEmail;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
