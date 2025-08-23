package com.example.SalesRecordManagement.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Authentication auth = (Authentication) request.getUserPrincipal();

        String currentRole = (auth != null && auth.getAuthorities().size() > 0)
                ? auth.getAuthorities().iterator().next().getAuthority()
                : "UNKNOWN";

        String requiredRole = extractRequiredRole(request.getRequestURI());

        Map<String, String> body = new HashMap<>();
        body.put("error", "Forbidden");
        body.put("message", "You are " + currentRole + ", not authorised for " + requiredRole + "'s page");

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

    private String extractRequiredRole(String uri) {
        if (uri.startsWith("/admin")) return "ADMIN";
        if (uri.startsWith("/analyst")) return "ANALYST";
        if (uri.startsWith("/viewer")) return "VIEWER";
        if (uri.startsWith("/customer")) return "CUSTOMER";
        return "UNKNOWN";
    }
}
