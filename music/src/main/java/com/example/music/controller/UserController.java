package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.User;
import com.example.music.service.impl.UserServiceImpl;
import com.example.music.utils.EncryptionUtils;
import com.example.music.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @PostMapping("/register")
    public Object addUser(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String email = req.getParameter("email").trim();

        if ("".equals(username)) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Register error! Username should not be empty.");
            return jsonObject;
        }

        String encryptedPassword;
        try {
            encryptedPassword = EncryptionUtils.encrypt(password);
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Register error! Password encryption failed.");
            return jsonObject;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmail(email);

        try {
            boolean res = userService.addUser(user);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("success", true);
                jsonObject.put("msg", "Register success! Please login.");
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("success", false);
                jsonObject.put("msg", "Register failed! Please try again.");
            }
            return jsonObject;
        } catch (DuplicateKeyException e) {
            jsonObject.put("code", 2);
            jsonObject.put("success", false);
            jsonObject.put("msg", "Register failed! Username already exists.");
            return jsonObject;
        }
    }

    @PostMapping("/update")
    public Object updateUserInfo(HttpServletRequest req) {
        JSONObject jsonObject=new JSONObject();
        String id = req.getParameter("id").trim();
        String username=req.getParameter("username").trim();
        String password=req.getParameter("password").trim();
        String email=req.getParameter("email").trim();

        User user=new User();
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        boolean res=userService.updateUserInfo(user);
        if(res){
            jsonObject.put("code",1);
            jsonObject.put("msg", "Update success!");
        } else{
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Update fails! Please try again.");
        }
        return jsonObject;
    }

    @PostMapping("/login")
    public CompletableFuture<JSONObject> loginStatus(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();

        return CompletableFuture.supplyAsync(() -> userService.userlogin(username, password), executor)
                .thenCompose(user -> {
                    JSONObject jsonObject = new JSONObject();

                    if (user != null) {
                        jsonObject.put("code", 1);
                        jsonObject.put("success", true);
                        jsonObject.put("msg", "Login success!");
                        jsonObject.put("session", session.getId());

                        return CompletableFuture.runAsync(() -> {
                            try {
                                SessionUtils.setEncryptedAttribute(session, "userId", String.valueOf(user.getId()));
                            } catch (Exception e) {
                                throw new RuntimeException("Encryption error.", e);
                            }
                        }, executor).thenApply(v -> jsonObject);
                    } else {
                        jsonObject.put("code", 0);
                        jsonObject.put("success", false);
                        jsonObject.put("msg", "Login error! Please check your username or password.");
                        return CompletableFuture.completedFuture(jsonObject);
                    }
                }).exceptionally(e -> {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("code", 0);
                    errorResponse.put("success", false);
                    errorResponse.put("msg", e.getMessage());
                    return errorResponse;
                });
    }

    @PostMapping("/delete")
    public Object deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");
        return userService.deleteUser(Integer.parseInt(id));
    }

    @PostMapping("/logout")
    public Object logout(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        try {
            session.removeAttribute("userId");
            jsonObject.put("code", 1);
            jsonObject.put("msg", "Logout success!");
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred during logout.");
        }
        return jsonObject;
    }

    @GetMapping("/all")
    public Object allUser() {
        return userService.allUser();
    }

    @GetMapping("/info")
    public Object userInfo(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        int id = -1;
        try {
            String encryptedId = (String) session.getAttribute("userId");
            if (encryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Session expired. Please log in again.");
                return jsonObject;
            }
            String decryptedId = SessionUtils.getDecryptedAttribute(session, "userId");
            if (decryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Decryption error. Please log in again.");
                return jsonObject;
            }
            id = Integer.parseInt(decryptedId);
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred while processing your request. Please log in again.");
            return jsonObject;
        }
        return userService.userInfo(id);
    }
}


