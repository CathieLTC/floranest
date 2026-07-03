package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.User;
import com.example.floranest.backend.mapper.UserMapper;
import com.example.floranest.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    public boolean register(User user) {

        User existingUser = userMapper.findByEmail(user.getEmail());

        if (existingUser != null) {
            return false;
        }

        user.setRole("CUSTOMER");

        return userMapper.insert(user) > 0;
    }

    @Override
    public User login(String email, String password) {

        User user = userMapper.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userMapper.delete(userId) > 0;
    }
}