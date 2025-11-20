package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final Services service;

    public AdminController(Services service) {
        this.service = service;
    }

    // 3. Admin login
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {
        String username = admin.getUsername();
        String password = admin.getPassword();
        return service.validateAdmin(username, password);
    }
}

