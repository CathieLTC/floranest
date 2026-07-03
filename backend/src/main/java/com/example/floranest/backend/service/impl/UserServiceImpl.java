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
    public User getUserById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public boolean register(User user) {

        User existingUser = userMapper.findByEmail(user.getEmail());

        if (existingUser != null) {
            return false;
        }

        user.setRole("CUSTOMER");

        userMapper.insert(user);

        return true;
    }

    @Override
    public boolean login(String email, String password) {

        User user = userMapper.findByEmail(email);

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);

    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.delete(id);
    }

}