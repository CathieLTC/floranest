package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Integer userId);
    boolean register(User user);
    User login(String email, String password);
    boolean updateUser(User user);
    boolean deleteUser(Integer userId);

}