package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTO.RegisterRequest;
import com.example.SalesRecordManagement.DTOResponse.UserListResponse;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.AdminProfileRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AdminProfileRepository adminProfileRepository;

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

}
