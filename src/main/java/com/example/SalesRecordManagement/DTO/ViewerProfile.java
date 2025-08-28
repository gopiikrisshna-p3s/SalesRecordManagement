package com.example.SalesRecordManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ViewerProfile {
    private String viewerName;
    private String viewerEmail;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
