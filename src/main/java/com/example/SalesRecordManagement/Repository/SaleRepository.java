package com.example.SalesRecordManagement.Repository;

import com.example.SalesRecordManagement.DTOResponse.ProductSalesReportResponse;
import com.example.SalesRecordManagement.Entity.Sale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    @Query("SELECT s.product,SUM(s.quantity) as totalSale "+
            "FROM Sale s WHERE s.product.company.companyId=:companyId "+
            "GROUP BY s.product " +
            "ORDER BY totalSale DESC")
    List<Object[]> findTopProductsByCompany(@Param("companyId") UUID companyId);

    @Query("SELECT p.productId, p.productName, p.category,s.date, " +
            "SUM(s.quantity), SUM(s.price), " +
            "SUM(s.revenue), SUM(s.cost), SUM(s.profit), s.region " +
            "FROM Sale s " +
            "JOIN s.product p " +
            "WHERE p.productId = :productId " +
            "GROUP BY p.productId, p.productName, p.category, s.date, s.region " +
            "ORDER BY s.date ASC")
    List<Object[]> getSalesReportByProduct(@Param("productId") UUID productId);

    @Query("SELECT p.productId, p.productName, p.category, s.date, s.quantity, s.price, s.revenue, s.cost, s.profit, s.region " +
            "FROM Sale s " +
            "JOIN s.product p " +
            "WHERE p.productId = :productId " +
            "ORDER BY s.date DESC")
    List<Object[]> getRecentSalesByProduct(@Param("productId") UUID productId);

    @Query("SELECT c.companyId, c.companyName, SUM(s.profit) as totalProfit " +
            "FROM Sale s " +
            "JOIN s.product p " +
            "JOIN p.company c " +
            "GROUP BY c.companyId, c.companyName " +
            "ORDER BY totalProfit DESC LIMIT 5")
    List<Object[]> findTopCompaniesByProfit(PageRequest pageRequest);

    @Query("SELECT new com.example.SalesRecordManagement.DTOResponse.ProductSalesReportResponse(" +
            "p.productId, p.productName, p.category, s.date, " +
            "s.quantity, s.price, s.revenue, s.cost, s.profit, s.region) " +
            "FROM Sale s " +
            "JOIN s.product p " +
            "WHERE s.region = :region")
    List<ProductSalesReportResponse> findSalesByRegion(@Param("region") String region);


}
