package com.example.SalesRecordManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;

    @Column(unique = true)
    private String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode queryConfig;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private Users user;
}
