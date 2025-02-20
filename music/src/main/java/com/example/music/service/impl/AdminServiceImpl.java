package com.example.music.service.impl;

import com.example.music.dao.AdminMapper;
import com.example.music.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminMapper AdminMapper;
    @Override
    public boolean adminlogin(String adminname, String password){
        return AdminMapper.adminlogin(adminname,password)>0;
    }
}
