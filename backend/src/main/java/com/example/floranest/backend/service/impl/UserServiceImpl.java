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
    public List<User> getAllUsers(){
        List<User> users = userMapper.findAll();
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    @Override
    public User getUserById(Integer userId) {

        User user = userMapper.findById(userId);

        if(user != null){
            user.setPassword(null);
        }

        return user;
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

        User existing = userMapper.findById(user.getUserId());

        if(existing == null){
            return false;
        }

        // Keep sensitive values
        user.setPassword(existing.getPassword());
        user.setRole(existing.getRole());

        return userMapper.update(user) > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userMapper.delete(userId) > 0;
    }

    @Override
    public boolean changePassword(
            Integer userId,
            String currentPassword,
            String newPassword) {

        User user = userMapper.findById(userId);

        if (user == null) {
            return false;
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        return userMapper.update(user) > 0;
    }
}