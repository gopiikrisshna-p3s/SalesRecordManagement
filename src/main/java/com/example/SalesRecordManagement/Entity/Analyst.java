package com.example.SalesRecordManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Analyst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analystId;
    private String name;
    private String email;

    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;
}
