package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.AdminProfile;
import com.example.SalesRecordManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile,Long> {
    AdminProfile findByUser(Users user);
}
