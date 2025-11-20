package com.project.back_end.mvc;


import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    private final Services service;

    @Autowired
    public DashboardController(Services service) {
        this.service = service;
    }

    // Admin dashboard route
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        boolean valid = service.validateToken(token, "admin");
        if (valid) {
            return "admin/adminDashboard"; // Thymeleaf view for admin
        } else {
            return "redirect:/"; // Invalid token, redirect to home/login
        }
    }

    // Doctor dashboard route
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        boolean valid = service.validateToken(token, "doctor");
        if (valid) {
            return "doctor/doctorDashboard"; // Thymeleaf view for doctor
        } else {
            return "redirect:/"; // Invalid token, redirect to home/login
        }
    }
}
