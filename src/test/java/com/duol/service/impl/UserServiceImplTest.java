package com.duol.service.impl;

import com.duol.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Duolaimon
 * 18-7-19 下午4:18
 */
@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public void logout() {
        userService.logout("23");
    }
}