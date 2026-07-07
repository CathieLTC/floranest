package com.example.floranest.backend.service.impl;

import com.example.floranest.backend.entity.User;
import com.example.floranest.backend.mapper.UserMapper;
import com.example.floranest.backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

        User existing = userMapper.findByEmail(user.getEmail());
        if (existing != null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("USER");

        return userMapper.insert(user) > 0;
    }

    @Override
    public User login(String email, String password) {

        User user = userMapper.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
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