package com.example.SalesRecordManagement.DTOResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListResponse {
    private String username;
    private String role;
}
