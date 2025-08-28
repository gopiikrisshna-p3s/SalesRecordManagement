package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTO.AuthRequest;
import com.example.SalesRecordManagement.DTOResponse.AuthResponse;
import com.example.SalesRecordManagement.DTO.RegisterRequest;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsersRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest registerRequest){
        Users user = Users.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        userRepo.save(user);
        return "User registered successfully";
    }

    public AuthResponse authenticate(AuthRequest authRequest){
        Users user = userRepo.findByUsername(authRequest.getUsername());
        if(user==null){
            throw new UsernameNotFoundException("Username not found");
        }
        if(!passwordEncoder.matches(authRequest.getPassword(),user.getPassword())){
            throw new UsernameNotFoundException("Wrong password");
        }
        String token =jwtUtil.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }




}
