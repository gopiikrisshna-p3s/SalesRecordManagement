package com.example.SalesRecordManagement.Controller;
import com.example.SalesRecordManagement.DTOResponse.CustomerProfileResponse;
import com.example.SalesRecordManagement.DTOResponse.ProductSalesReportResponse;
import com.example.SalesRecordManagement.DTOResponse.ProductSalesResponse;
import com.example.SalesRecordManagement.Entity.Customer;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.CustomerRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import com.example.SalesRecordManagement.Service.CustomerService;
import com.example.SalesRecordManagement.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        fileUploadService.processFileUpload(file, users);

        return ResponseEntity.ok("Data saved Successfully");
    }

    @GetMapping("/profile/{id}")
    public CustomerProfileResponse profile(@PathVariable Long id){
        Customer customer = customerRepository.findById(id).orElseThrow();
        return customerService.getCustomerProfile(customer);
    }

    @GetMapping("/sale-by-company/{id}")
    public List<ProductSalesResponse> topProduct(@PathVariable Long id){
       return customerService.getTopProductsByCompany(id);
    }

    @GetMapping("/sale-by-product/{id}")
    public List<ProductSalesReportResponse> getSalesReport(@PathVariable Long id){
        return customerService.getSalesReportByProduct(id);
    }
    @GetMapping("/products/{productId}/recent-sales")
    public ResponseEntity<List<ProductSalesReportResponse>> getRecentSales(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "3") int limit) {

        return ResponseEntity.ok(customerService.getRecentSalesByProduct(productId, limit));
    }
}