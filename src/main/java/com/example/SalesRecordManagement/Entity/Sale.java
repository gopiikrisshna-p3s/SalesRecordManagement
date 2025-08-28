package com.example.SalesRecordManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID saleId;
    private LocalDate date;
    private Integer quantity;
    private Double price;
    private Double revenue;
    private Double cost;
    private Double profit;
    private String region;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private Customer customer;


}
