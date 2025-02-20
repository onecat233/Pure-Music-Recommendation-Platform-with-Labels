package com.example.music;

import com.example.music.domain.User;
import com.example.music.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void UserTest(){

        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setEmail("1239679@qq.com");
        userService.addUser(user);
    }

}