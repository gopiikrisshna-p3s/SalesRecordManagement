package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTO.AnalystProfile;
import com.example.SalesRecordManagement.DTO.ViewerProfile;
import com.example.SalesRecordManagement.DTOResponse.ProductSalesReportResponse;
import com.example.SalesRecordManagement.Entity.*;
import com.example.SalesRecordManagement.Repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnalystService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private AnalystRepository analystRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuditLogsRepository auditLogsRepository;

    public String addProfile(AnalystProfile analystProfile, UUID id){
        Users users = usersRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found!"));
        Analyst analyst = Analyst.builder()
                .user(users)
                .name(analystProfile.getAnalystName())
                .email(analystProfile.getAnalystEmail())
                .Address(analystProfile.getAddress())
                .dateOfBirth(analystProfile.getDateOfBirth())
                .phone(analystProfile.getPhone())
                .build();
        analystRepository.save(analyst);
        return "Analyst Profile Saved Successfully!";
    }

    public String updateProfile(AnalystProfile analystProfile, UUID id){
        Users users = usersRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found!"));
        Analyst analyst = analystRepository.findByUser(users);
        analyst.setName(analystProfile.getAnalystName());
        analyst.setEmail(analystProfile.getAnalystEmail());
        analyst.setAddress(analystProfile.getAddress());
        analyst.setDateOfBirth(analystProfile.getDateOfBirth());
        analyst.setPhone(analystProfile.getPhone());
        analystRepository.save(analyst);
        return "Analyst Profile Saved Successfully!";
    }

    public List<ProductSalesReportResponse> getSalesReportByRegion(String region) {
        return saleRepository.findSalesByRegion(region);
    }

    public Report saveReport(String name, JsonNode responseData, Users user) {
        Report report = Report.builder()
                .name(name)
                .queryConfig(responseData)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        AuditLogs audit = AuditLogs.builder()
                .user(user)
                .action("Uploaded Report for " + name)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogsRepository.save(audit);

        return reportRepository.save(report);
    }

    public Report getReportByName(String name,Users user,String action) {
        Report report = reportRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Report not found with name: " + name));

        AuditLogs audit = AuditLogs.builder()
                .user(user)
                .action(action+" Report of " + name)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogsRepository.save(audit);

        return report;
    }

}
