package com.example.floranest.backend.service;

import com.example.floranest.backend.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Integer id);
    User findByEmail(String email);
    boolean register(User user);
    boolean login(String email, String password);
    void updateUser(User user);
    void deleteUser(Integer id);

}