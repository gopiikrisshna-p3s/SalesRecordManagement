package com.example.SalesRecordManagement.Service;

import com.example.SalesRecordManagement.DTO.ViewerProfile;
import com.example.SalesRecordManagement.DTOResponse.CompanyProfitResponse;
import com.example.SalesRecordManagement.DTOResponse.CompanyResponse;
import com.example.SalesRecordManagement.DTOResponse.ProductResponse;
import com.example.SalesRecordManagement.Entity.Company;
import com.example.SalesRecordManagement.Entity.Users;
import com.example.SalesRecordManagement.Entity.Viewer;
import com.example.SalesRecordManagement.Repository.CompanyRepository;
import com.example.SalesRecordManagement.Repository.SaleRepository;
import com.example.SalesRecordManagement.Repository.UsersRepository;
import com.example.SalesRecordManagement.Repository.ViewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ViewerService {
    @Autowired
    private ViewerRepository viewerRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public String addProfile(ViewerProfile viewerProfile, UUID id){
        Users users = usersRepository.findById(id).orElseThrow();
        Viewer viewer = Viewer.builder()
                .user(users)
                .name(viewerProfile.getViewerName())
                .email(viewerProfile.getViewerEmail())
                .Address(viewerProfile.getAddress())
                .dateOfBirth(viewerProfile.getDateOfBirth())
                .phone(viewerProfile.getPhone())
                .build();
        viewerRepository.save(viewer);
        return "Viewer Profile Saved Successfully!";
    }

    public String updateProfile(ViewerProfile viewerProfile, UUID id){
        Users users = usersRepository.findById(id).orElseThrow();
        Viewer viewer = viewerRepository.findByUser(users);
        viewer.setName(viewerProfile.getViewerName());
        viewer.setEmail(viewerProfile.getViewerEmail());
        viewer.setAddress(viewerProfile.getAddress());
        viewer.setDateOfBirth(viewerProfile.getDateOfBirth());
        viewer.setPhone(viewerProfile.getPhone());
        viewerRepository.save(viewer);
        return "Viewer Profile Updated Successfully!";
    }

    public List<CompanyProfitResponse> getTopCompaniesByProfit() {
        List<Object[]> results = saleRepository.findTopCompaniesByProfit(PageRequest.of(0, 5));

        return results.stream()
                .map(obj -> new CompanyProfitResponse(
                        ((UUID) obj[0]),
                        (String) obj[1],
                        ((Number) obj[2]).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    public CompanyResponse getCompanyDetailsWithProducts(String companyName) {
        Company company = companyRepository.findCompanyWithProducts(companyName);

        List<ProductResponse> productResponses = company.getProducts()
                .stream()
                .map(p -> ProductResponse.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .category(p.getCategory())
                        .build())
                .toList();

        // Build final response
        return CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .products(productResponses)
                .build();
    }

}
