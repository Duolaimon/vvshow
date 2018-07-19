package com.duol.service.impl;

import com.duol.dao.UserMapper;
import com.duol.pojo.User;
import com.duol.util.MD5Util;
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
    private UserMapper userMapper;

    @Test
    public void testLogin() {
        String md5Password = MD5Util.MD5EncodeUtf8("797979");
        String username = "duolaimon";
        User id = userMapper.selectLogin(username,md5Password);
        System.out.println(id.getId());
    }
}