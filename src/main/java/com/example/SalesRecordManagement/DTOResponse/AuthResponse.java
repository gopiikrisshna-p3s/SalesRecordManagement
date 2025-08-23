package com.example.SalesRecordManagement.DTOResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
}
