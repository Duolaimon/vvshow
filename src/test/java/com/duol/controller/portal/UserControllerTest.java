package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.pojo.User;
import com.duol.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
 */

/**
 * @author Duolaimon
 * 18-7-17 下午5:03
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void registerTest() throws Exception {
        User user = new User();
        user.setUsername("mock2");
        user.setPassword("123456");
        user.setAnswer("你爸爸");
        user.setQuestion("你是谁");
        user.setEmail("hjg129y7@qq.com");
        user.setPhone("12435859671");
        Gson gson = new GsonBuilder().create();
        String userJson = gson.toJson(user);
        mockMvc.perform(post("/user")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "\"status\": 0,\n" +
                        "\"msg\": \"注册成功\",\n" +
                        "\"data\": null,\n" +
                        "\"success\": true\n" +
                        "}"));
    }

    @Test
    public void loginTest() throws Exception {
        String username = "mock2";
        String password = "654321";
        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("username", username)
                .param("password", password))
                .andDo(print());
    }

    @Test
    public void resetPasswordTest() throws Exception {
        User user = new User();
        user.setUsername("mock2");
        user.setId(25);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(Const.CURRENT_USER,user);
        mockMvc.perform(put("/user/{userId}/password",25)
                .session(session)
                .param("newPassword", "654321")
                .param("oldPassword", "123456"))
                .andDo(print());
    }
}