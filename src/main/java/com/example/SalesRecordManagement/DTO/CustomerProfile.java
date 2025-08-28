package com.example.SalesRecordManagement.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerProfile {
    private String customerName;
    private String email;
    private String address;
    private LocalDate birthDate;
    private String phone;
}
