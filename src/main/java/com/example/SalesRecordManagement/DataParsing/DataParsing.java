package com.example.SalesRecordManagement.DataParsing;

import com.example.SalesRecordManagement.DTO.CompanyDto;
import com.example.SalesRecordManagement.DTO.CustomerDto;
import com.example.SalesRecordManagement.DTO.ProductDto;
import com.example.SalesRecordManagement.DTO.SaleDto;
import com.example.SalesRecordManagement.Entity.*;
import com.example.SalesRecordManagement.Repository.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
@Component
public class DataParsing {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AuditLogsRepository auditLogsRepository;

    public void processCSV(MultipartFile file, Map<String, Integer> headerMap, Users users) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            boolean firstLine = true;

            while ((line = csvReader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                CustomerDto customerDto = new CustomerDto(
                        safeGet(line, headerMap.get("CustomerName")),
                        safeGet(line, headerMap.get("Email"))
                );

                CompanyDto companyDto = new CompanyDto(
                        safeGet(line, headerMap.get("CompanyName")),
                        safeGet(line, headerMap.get("CompanyAddress"))
                );

                ProductDto productDto = new ProductDto(
                        safeGet(line, headerMap.get("ProductName")),
                        safeGet(line, headerMap.get("Category")),
                        safeGet(line, headerMap.get("Code"))
                );

                // ---- Date Parsing ----
                LocalDate saleDate = null;
                String dateStr = safeGet(line, headerMap.get("SaleDate"));
                if (!dateStr.isEmpty()) {
                    try {
                        saleDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid SaleDate: " + dateStr);
                    }
                }

                LocalDateTime saleTimestamp = null;
                String tsStr = safeGet(line, headerMap.get("SaleTimestamp"));
                if (!tsStr.isEmpty()) {
                    try {
                        // Try ISO format first (yyyy-MM-ddTHH:mm:ss)
                        saleTimestamp = LocalDateTime.parse(tsStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (DateTimeParseException e1) {
                        try {
                            // Try space-based format (yyyy-MM-dd HH:mm:ss)
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            saleTimestamp = LocalDateTime.parse(tsStr, formatter);
                        } catch (DateTimeParseException e2) {
                            System.err.println("Invalid SaleTimestamp: " + tsStr);
                        }
                    }
                }


                SaleDto saleDto = SaleDto.builder()
                        .productName(safeGet(line, headerMap.get("ProductName")))
                        .category(safeGet(line, headerMap.get("Category")))
                        .code(safeGet(line, headerMap.get("Code")))
                        .action(safeGet(line, headerMap.get("SaleAction")))
                        .timestamp(saleTimestamp)
                        .date(saleDate)
                        .quantity(parseIntSafe(safeGet(line, headerMap.get("Quantity"))))
                        .price(parseDoubleSafe(safeGet(line, headerMap.get("Price"))))
                        .revenue(parseDoubleSafe(safeGet(line, headerMap.get("Revenue"))))
                        .cost(parseDoubleSafe(safeGet(line, headerMap.get("Cost"))))
                        .profit(parseDoubleSafe(safeGet(line, headerMap.get("Profit"))))
                        .region(safeGet(line, headerMap.get("Region")))
                        .build();

                saveToDb(customerDto, companyDto, productDto, saleDto, users);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


    public void processExcel(MultipartFile file, Map<String, Integer> headerMap,Users users) throws IOException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                CustomerDto customerDto = new CustomerDto(
                        row.getCell(headerMap.get("CustomerName")).getStringCellValue(),
                        row.getCell(headerMap.get("Email")).getStringCellValue()
                );
                CompanyDto companyDto = new CompanyDto(
                        row.getCell(headerMap.get("CompanyName")).getStringCellValue(),
                        row.getCell(headerMap.get("CompanyAddress")).getStringCellValue()
                );
                ProductDto productDto = new ProductDto(
                        row.getCell(headerMap.get("ProductName")).getStringCellValue(),
                        row.getCell(headerMap.get("Category")).getStringCellValue(),
                        row.getCell(headerMap.get("Code")).getStringCellValue()
                );

                LocalDate saleDate = null;
                Cell dateCell = row.getCell(headerMap.get("SaleDate"));
                if (dateCell != null) {
                    if (DateUtil.isCellDateFormatted(dateCell)) {
                        saleDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
                    } else {
                        saleDate = LocalDate.parse(getCellValueAsString(dateCell,evaluator));
                    }
                }


                LocalDateTime saleTimestamp = null;
                Cell tsCell = row.getCell(headerMap.get("SaleTimestamp"));
                if (tsCell != null && DateUtil.isCellDateFormatted(tsCell)) {
                    saleTimestamp = tsCell.getLocalDateTimeCellValue();
                }

                SaleDto saleDto = SaleDto.builder()
                        .productName(getCellValueAsString(row.getCell(headerMap.get("ProductName")),evaluator))
                        .category(getCellValueAsString(row.getCell(headerMap.get("Category")),evaluator))
                        .code(getCellValueAsString(row.getCell(headerMap.get("Code")),evaluator))
                        .action(getCellValueAsString(row.getCell(headerMap.get("SaleAction")),evaluator))
                        .timestamp(saleTimestamp)
                        .date(saleDate)
                        .quantity((int) Double.parseDouble(getCellValueAsString(row.getCell(headerMap.get("Quantity")), evaluator)))
                        .price(Double.parseDouble(getCellValueAsString(row.getCell(headerMap.get("Price")), evaluator)))
                        .revenue(Double.parseDouble(getCellValueAsString(row.getCell(headerMap.get("Revenue")), evaluator)))
                        .cost(Double.parseDouble(getCellValueAsString(row.getCell(headerMap.get("Cost")), evaluator)))
                        .profit(Double.parseDouble(getCellValueAsString(row.getCell(headerMap.get("Profit")), evaluator)))
                        .region(getCellValueAsString(row.getCell(headerMap.get("Region")),evaluator))
                        .build();

                saveToDb(customerDto, companyDto, productDto, saleDto, users);
            }
        }
    }

    private String safeGet(String[] line, Integer index) {
        if (index == null || index >= line.length || line[index] == null) return "";
        return line[index].trim();
    }

    private int parseIntSafe(String value) {
        try {
            return (value == null || value.isEmpty()) ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return (value == null || value.isEmpty()) ? 0.0 : Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    private String getCellValueAsString(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case NUMERIC:
                        return String.valueOf(cellValue.getNumberValue());
                    case STRING:
                        return cellValue.getStringValue();
                    case BOOLEAN:
                        return String.valueOf(cellValue.getBooleanValue());
                    default:
                        return "";
                }
            case BLANK:
            default:
                return "";
        }
    }

    private void saveToDb(CustomerDto customerDto, CompanyDto companyDto, ProductDto productDto, SaleDto saleDto, Users users) {

        Customer customer = customerRepository.findByUser(users)
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .customerName(customerDto.getCustomerName())
                            .email(customerDto.getEmail())
                            .user(users)
                            .build();
                    return customerRepository.save(newCustomer);
                });

        Company company = companyRepository.findByCompanyNameAndCustomer(companyDto.getCompanyName(), customer)
                .orElseGet(() -> {
                    Company newCompany = Company.builder()
                            .companyName(companyDto.getCompanyName())
                            .companyAddress(companyDto.getCompanyAddress())
                            .customer(customer)
                            .build();
                    return companyRepository.save(newCompany);
                });

        Product product = productRepository.findByCodeAndCompany(productDto.getCode(), company)
                .orElseGet(() -> {
                    Product newProduct = Product.builder()
                            .productName(productDto.getProductName())
                            .category(productDto.getCategory())
                            .code(productDto.getCode())
                            .company(company)
                            .build();
                    return productRepository.save(newProduct);
                });

        Sale sale = Sale.builder()
                .product(product)
                .customer(customer)
                .date(saleDto.getDate())
                .quantity(saleDto.getQuantity())
                .price(saleDto.getPrice())
                .revenue(saleDto.getRevenue())
                .cost(saleDto.getCost())
                .profit(saleDto.getProfit())
                .region(saleDto.getRegion())
                .build();
        saleRepository.save(sale);

        AuditLogs audit = AuditLogs.builder()
                .user(users)
                .action("Uploaded sales record for " + productDto.getProductName() +" of "+companyDto.getCompanyName())
                .timestamp(LocalDateTime.now())
                .build();
        auditLogsRepository.save(audit);
    }
}
