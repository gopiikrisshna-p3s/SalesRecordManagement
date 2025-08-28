package com.example.SalesRecordManagement.DTOResponse;

import com.example.SalesRecordManagement.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    private UUID productId;
    private String productName;
    private String category;
}
