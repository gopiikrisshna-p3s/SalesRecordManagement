package com.example.SalesRecordManagement.DTOResponse;

import com.example.SalesRecordManagement.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productName;
    private String category;
}
