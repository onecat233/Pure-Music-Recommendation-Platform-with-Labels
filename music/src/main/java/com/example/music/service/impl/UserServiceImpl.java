package com.example.music.service.impl;

import com.example.music.dao.UserMapper;
import com.example.music.domain.User;
import com.example.music.service.UserService;
import com.example.music.utils.EncryptionUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean addUser(User user){
        return userMapper.addUser(user)>0;
    }

    @Override
    public boolean updateUserInfo(User user){
        return userMapper.updateUserInfo(user)>0;
    }

    @Override
    public boolean searchUser(String username){
        return userMapper.searchUser(username)>0;
    }

    @Override
    public User userlogin(@Param("username") String username, @Param("password")String password){
        try {
            password = EncryptionUtils.encrypt(password);
            return userMapper.userlogin(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteUser(Integer userid){
        return userMapper.deleteUser(userid)>0;
    }

    @Override
    public List<User> allUser(){
        return userMapper.allUser();
    }
    @Override
    public List<User> userInfo(Integer userid){
        return userMapper.userInfo(userid);
    }
}
