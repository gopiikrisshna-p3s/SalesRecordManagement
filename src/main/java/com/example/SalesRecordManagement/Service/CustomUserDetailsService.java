package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepo.findByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException("Username not found");
        }
    return User.builder()
            .username(users.getUsername())
            .password(users.getPassword())
            .roles(users.getRole())
            .build();
    }
}
