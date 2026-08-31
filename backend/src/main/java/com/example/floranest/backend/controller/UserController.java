package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.User;
import com.example.floranest.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Integer id,
                             @RequestBody User user) {
        user.setUserId(id);
        userService.updateUser(user);
        return "User updated successfully.";
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable Integer id,
            @RequestBody Map<String,String> body){

        boolean success = userService.changePassword(
                id,
                body.get("currentPassword"),
                body.get("newPassword")
        );

        if(success){
            return ResponseEntity.ok("Password changed successfully.");
        }

        return ResponseEntity
                .badRequest()
                .body("Current password is incorrect.");

    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted successfully.";
    }

}