package com.example.SalesRecordManagement.Controller;

import com.example.SalesRecordManagement.DTO.RegisterRequest;
import com.example.SalesRecordManagement.DTOResponse.UserListResponse;
import com.example.SalesRecordManagement.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

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
}
