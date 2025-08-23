package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile,Long> {
}
