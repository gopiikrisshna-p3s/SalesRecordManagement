package com.example.SalesRecordManagement.Controller;

import com.example.SalesRecordManagement.DTO.RegisterRequest;
import com.example.SalesRecordManagement.DTO.ReportRequest;
import com.example.SalesRecordManagement.DTOResponse.*;
import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Report;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.CustomerRepository;
import com.example.SalesRecordManagement.Repository.ReportRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import com.example.SalesRecordManagement.Service.AdminService;
import com.example.SalesRecordManagement.Service.AnalystService;
import com.example.SalesRecordManagement.Service.CustomerService;
import com.example.SalesRecordManagement.Service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AnalystService analystService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ViewerService viewerService;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/create-user")
    public String createUser(@RequestBody RegisterRequest registerRequest){
        return adminService.createUser(registerRequest);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserListResponse>> getAllUsers() {
        List<UserListResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/deactivate/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok("User deactivated successfully!");
    }
    @GetMapping("/sale-by-company/{id}")
    public List<ProductSalesResponse> topProduct(@PathVariable Long id){
        return customerService.getTopProductsByCompany(id);
    }

    @GetMapping("/sale-by-product/{id}")
    public List<ProductSalesReportResponse> getSalesReport(@PathVariable Long id){
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

    @GetMapping("/top-companies")
    public ResponseEntity<List<CompanyProfitResponse>> getTopCompanies(){
        return ResponseEntity.ok(viewerService.getTopCompaniesByProfit());
    }

    @GetMapping("/company/{name}")
    public ResponseEntity<CompanyResponse> getCompanyByName(@PathVariable String name) {
        return ResponseEntity.ok(viewerService.getCompanyDetailsWithProducts(name));
    }

    @GetMapping("/customer/{id}")
    public CustomerProfileResponse profile(@PathVariable Long id){
        Customer customer = customerRepository.findById(id).orElseThrow();
        return customerService.getCustomerProfile(customer);
    }

    @GetMapping("/getLogDetails")
    public List<AuditLogsResponse> getLogDetails() {
        return adminService.getLogDetails();
    }
    @GetMapping("/getLogDetails/byDate")
    public List<AuditLogsResponse> getLogDetailsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return adminService.getLogDetailsbyDate(startDate,endDate);
    }
}
