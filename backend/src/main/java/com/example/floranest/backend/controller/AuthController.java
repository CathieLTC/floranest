package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.User;
import com.example.floranest.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

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
}