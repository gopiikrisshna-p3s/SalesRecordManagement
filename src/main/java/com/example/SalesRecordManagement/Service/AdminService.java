package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTO.AdminProfileDto;
import com.example.SalesRecordManagement.DTO.RegisterRequest;
import com.example.SalesRecordManagement.DTOResponse.AuditLogsResponse;
import com.example.SalesRecordManagement.DTOResponse.UserListResponse;
import com.example.SalesRecordManagement.Entity.AdminProfile;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.AdminProfileRepository;
import com.example.SalesRecordManagement.Repository.AuditLogsRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuditLogsRepository auditLogsRepository;
    @Autowired
    private AdminProfileRepository adminProfileRepository;

    public String addProfile(Long id, AdminProfileDto adminProfileDto){
        Users users = usersRepository.findById(id).get();
        AdminProfile  adminProfile = AdminProfile.builder()
                .name(adminProfileDto.getName())
                .email(adminProfileDto.getEmail())
                .phone(adminProfileDto.getPhone())
                .user(users)
                .build();
        adminProfileRepository.save(adminProfile);
        return "Admin Profile added Successfully";
    }

    public String editProfile(Long id, AdminProfileDto adminProfileDto){
        Users users = usersRepository.findById(id).get();
        AdminProfile adminProfile = adminProfileRepository.findByUser(users);
        adminProfile.setName(adminProfileDto.getName());
        adminProfile.setEmail(adminProfileDto.getEmail());
        adminProfile.setPhone(adminProfileDto.getPhone());
        adminProfileRepository.save(adminProfile);
        return "Admin Profile Edited Successfully";
    }

    public String createUser(RegisterRequest registerRequest){
        Users users = Users.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        usersRepository.save(users);

        return "User Created!";
    }

    public List<UserListResponse> getAllUsers(){
       return usersRepository.findAll()
                    .stream().map(user -> UserListResponse.builder()
                       .username(user.getUsername())
                       .role(user.getRole())
                       .build())
               .collect(Collectors.toList());
    }

    public void deactivateUser(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        usersRepository.save(user);
    }

    public List<AuditLogsResponse> getLogDetails(){
        return auditLogsRepository.findAll()
                .stream().map(log -> AuditLogsResponse.builder()
                        .LogId(log.getLogId())
                        .action(log.getAction())
                        .timestamp(log.getTimestamp())
                        .username(log.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AuditLogsResponse> getLogDetailsbyDate(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogsRepository.findLogsByDateRange(startDate, endDate);
    }
}
