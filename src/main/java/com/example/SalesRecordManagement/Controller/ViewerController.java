package com.example.SalesRecordManagement.Controller;

import com.example.SalesRecordManagement.DTO.ViewerProfile;
import com.example.SalesRecordManagement.DTOResponse.*;
import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Report;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.CustomerRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import com.example.SalesRecordManagement.Service.AnalystService;
import com.example.SalesRecordManagement.Service.CustomerService;
import com.example.SalesRecordManagement.Service.ViewerService;
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
@RequestMapping("/viewer")
public class ViewerController {
    @Autowired
    private ViewerService viewerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AnalystService analystService;

    @PostMapping("/profile/{userId}")
    public String viewerProfile(@PathVariable UUID userId, @RequestBody ViewerProfile viewerProfile){
        return viewerService.addProfile(viewerProfile,userId);
    }

    @PutMapping("/editProfile/{userId}")
    public String editProfile(@PathVariable UUID userId, @RequestBody ViewerProfile viewerProfile){
        return viewerService.updateProfile(viewerProfile,userId);
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
    public CustomerProfileResponse profile(@PathVariable UUID id){
        Customer customer = customerRepository.findById(id).orElseThrow();
        return customerService.getCustomerProfile(customer);
    }

    @GetMapping("/sale-by-company/{id}")
    public List<ProductSalesResponse> topProduct(@PathVariable UUID id){
        return customerService.getTopProductsByCompany(id);
    }

    @GetMapping("/sale-by-product/{id}")
    public List<ProductSalesReportResponse> getSalesReport(@PathVariable UUID id){
        return customerService.getSalesReportByProduct(id);
    }

    @GetMapping("/products/{productId}/recent-sales")
    public ResponseEntity<List<ProductSalesReportResponse>> getRecentSales(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "3") int limit) {

        return ResponseEntity.ok(customerService.getRecentSalesByProduct(productId, limit));
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

}
