package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.AdminProfile;
import com.example.SalesRecordManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, UUID> {
    AdminProfile findByUser(Users user);
}
