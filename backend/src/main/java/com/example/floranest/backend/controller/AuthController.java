package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.User;
import com.example.floranest.backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Value("${app.admin.access-code:admin2026}")
    private String adminAccessCode;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {

        Map<String, Object> response = new HashMap<>();

        if (userService.register(user)) {

            response.put("success", true);
            response.put("message", "Registration successful.");

        } else {

            response.put("success", false);
            response.put("message", "Email already exists.");

        }

        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        Map<String, Object> response = new HashMap<>();

        User loggedInUser = userService.login(
                user.getEmail(),
                user.getPassword()
        );

        if (loggedInUser != null) {

            response.put("success", true);
            response.put("message", "Login successful.");

            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", loggedInUser.getUserId());
            userData.put("fullName", loggedInUser.getFullName());
            userData.put("email", loggedInUser.getEmail());
            userData.put("role", loggedInUser.getRole());

            response.put("user", userData);

        } else {

            response.put("success", false);
            response.put("message", "Invalid email or password.");

        }

        return response;
    }

    /**
     * Grants access to the Admin Dashboard by verifying a single password,
     * without creating any account. The frontend stores an "admin session"
     * flag after a successful check.
     */
    @PostMapping("/admin/access")
    public Map<String, Object> adminAccess(@RequestBody Map<String, String> body) {

        Map<String, Object> response = new HashMap<>();

        String password = body.get("password");

        if (password != null && adminAccessCode.equals(password)) {

            response.put("success", true);
            response.put("message", "Admin access granted.");

        } else {

            response.put("success", false);
            response.put("message", "Invalid admin access password.");

        }

        return response;
    }
}
