package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.Entity.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewerRepository extends JpaRepository<Viewer,Long> {
}
