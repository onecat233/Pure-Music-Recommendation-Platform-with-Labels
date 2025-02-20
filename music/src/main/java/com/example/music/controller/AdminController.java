package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.music.service.impl.AdminServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/login/status")
    public Object loginStatus(HttpServletRequest req, HttpSession session){
        JSONObject jsonObject = new JSONObject();
        String name=req.getParameter("name").trim();
        String password=req.getParameter("password").trim();
        boolean res = adminService.adminlogin(name, password);

        if(res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "Login success!");
            session.setAttribute("name", name);
            jsonObject.put("session", session.getId());
            return jsonObject;
        }
        else{
            jsonObject.put("code",0);
            jsonObject.put("msg","Login error! Please check your username or password again!");
            return jsonObject;
        }
    }

    @PostMapping("/logout")
    public Object logout(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        try {
            session.removeAttribute("name");
            jsonObject.put("code", 1);
            jsonObject.put("msg", "Logout success!");
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred during logout.");
        }
        return jsonObject;
    }
}
