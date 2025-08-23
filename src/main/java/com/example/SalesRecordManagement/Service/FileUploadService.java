package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DataParsing.DataParsing;
import com.example.SalesRecordManagement.Entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {
    @Autowired
    private DataParsing dataParsing;
        public void processFileUpload(MultipartFile file, Users users) throws IOException {
            String filename = file.getOriginalFilename();
            Map<String, Integer> headerMap = getHeaderMap();
            if (filename != null && filename.endsWith(".csv")) {
                dataParsing.processCSV(file, headerMap, users);
            } else if (filename != null && (filename.endsWith(".xls") || filename.endsWith(".xlsx"))) {
                dataParsing.processExcel(file, headerMap, users);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + filename);
            }
        }

    private Map<String, Integer> getHeaderMap() {
        return Map.ofEntries(
                Map.entry("CustomerName", 0),
                Map.entry("Email", 1),
                Map.entry("CompanyName", 2),
                Map.entry("CompanyAddress", 3),
                Map.entry("ProductName", 4),
                Map.entry("Category", 5),
                Map.entry("Code", 6),
                Map.entry("SaleAction", 7),
                Map.entry("SaleTimestamp", 8),
                Map.entry("SaleDate", 9),
                Map.entry("Quantity", 10),
                Map.entry("Price", 11),
                Map.entry("Revenue", 12),
                Map.entry("Cost", 13),
                Map.entry("Profit", 14),
                Map.entry("Region", 15)
        );
    }
}

