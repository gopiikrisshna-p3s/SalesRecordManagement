package com.example.SalesRecordManagement.Entity;

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
public class Analyst {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID analystId;
    private String name;
    private String email;
    private String Address;
    private LocalDate dateOfBirth;
    private String phone;

    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;
}
