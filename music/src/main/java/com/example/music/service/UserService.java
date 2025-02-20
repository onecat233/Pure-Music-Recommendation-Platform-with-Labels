package com.example.music.service;
import com.example.music.domain.User;

import java.util.List;

public interface UserService{
    boolean addUser (User user);
    boolean updateUserInfo (User user);
    boolean searchUser(String username);
    User userlogin(String username, String password);
    boolean deleteUser(Integer userid);

    List<User> allUser();
    List<User> userInfo(Integer userid);
}
