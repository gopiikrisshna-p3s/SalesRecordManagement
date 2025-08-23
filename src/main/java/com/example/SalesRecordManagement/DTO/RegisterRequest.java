package com.example.SalesRecordManagement.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
}
