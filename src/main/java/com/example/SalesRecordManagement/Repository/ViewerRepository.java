package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Entity.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViewerRepository extends JpaRepository<Viewer, UUID> {
    Viewer findByUser(Users users);
}
