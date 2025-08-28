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
public class AdminProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;

    private String name;
    private String email;
    private String phone;
    private String Address;
    private LocalDate dateOfBirth;

}
