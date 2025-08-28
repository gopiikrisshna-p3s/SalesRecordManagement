package com.example.SalesRecordManagement.Controller;

import com.example.SalesRecordManagement.DTO.AnalystProfile;
import com.example.SalesRecordManagement.DTO.ReportRequest;
import com.example.SalesRecordManagement.DTOResponse.ProductSalesReportResponse;
import com.example.SalesRecordManagement.DTOResponse.ProductSalesResponse;
import com.example.SalesRecordManagement.Entity.Report;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.ReportRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import com.example.SalesRecordManagement.Service.AnalystService;
import com.example.SalesRecordManagement.Service.CustomerService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analyst")
public class AnalystController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AnalystService analystService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ReportRepository reportRepository;

    @PostMapping("/profile/{userId}")
    public String analystProfile(@PathVariable UUID userId, @RequestBody AnalystProfile analystProfile){
        return analystService.addProfile(analystProfile,userId);
    }

    @PutMapping("/editProfile/{userId}")
    public String editProfile(@PathVariable UUID userId, @RequestBody AnalystProfile analystProfile){
        return analystService.updateProfile(analystProfile,userId);
    }

    @GetMapping("/sale-by-company/{id}")
    public List<ProductSalesResponse> topProduct(@PathVariable UUID id){
        return customerService.getTopProductsByCompany(id);
    }

    @GetMapping("/sale-by-product/{id}")
    public List<ProductSalesReportResponse> getSalesReport(@PathVariable UUID id){
        return customerService.getSalesReportByProduct(id);
    }

    @GetMapping("/sale-by-region/{region}")
    public List<ProductSalesReportResponse> getSalesReportByRegion(@PathVariable String region){
        return analystService.getSalesReportByRegion(region);
    }

    @PostMapping("/save-report/{username}")
    public ResponseEntity<Report> saveReport(@PathVariable String username,
                                             @RequestBody ReportRequest request) {

        Users user = usersRepository.findByUsername(username);

        Report saved = analystService.saveReport(
                request.getName(),
                request.getResponseData(),
                user
        );
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/update-report/{reportName}/username/{username}")
    public ResponseEntity<Report> updateReport(@PathVariable String reportName,@PathVariable String username,@RequestBody ReportRequest request) {
        Users user = usersRepository.findByUsername(username);
        String action = "updated";
        Report existingReport = analystService.getReportByName(reportName,user,action);
        existingReport.setName(request.getName());
        existingReport.setQueryConfig(request.getResponseData());
        reportRepository.save(existingReport);
        return ResponseEntity.ok(existingReport);
    }

    @GetMapping("/export/{reportName}/username/{username}")
    public ResponseEntity<byte[]> exportReport(@PathVariable String reportName,@PathVariable String username) throws IOException {
        Users user = usersRepository.findByUsername(username);
        String action = "exported";
        Report report = analystService.getReportByName(reportName,user,action);

        String jsonContent = report.getQueryConfig().toString();

        byte[] content = jsonContent.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", reportName + ".json");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @DeleteMapping("/report/{reportName}")
    public String deleteReport(@PathVariable String reportName) {
        if (reportRepository.findByName(reportName).isPresent()) {
            reportRepository.delete(reportRepository.findByName(reportName).get());
        }
        return "Report Deleted Successfully!";
    }

}
