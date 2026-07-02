package com.example.floranest.backend.mapper;

import com.example.floranest.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();
    User findById(Integer userId);
    int insert(User user);
    int update(User user);
    int delete(Integer userId);

}