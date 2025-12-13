package com.github.axinger.controller;

import com.github.axinger.model.dto.UserDTO;
import com.github.axinger.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//使用@WebMvcTest只实例化Web层，而不是整个上下文。在具有多个Controller的应用程序中，
// 甚至可以要求仅使用一个实例化，例如@WebMvcTest(UserController.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //模拟出一个userService
    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("tom")
                .age(10L)
                .build();
        // 指定返回结果
//        Mockito.when(userService.findById(1L)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tom"));
    }


    @Test
    public void test2() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("tom")
                .age(10L)
                .build();
        /// 指定返回结果
        Mockito.when(userService.findById(1L)).thenReturn(user);
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserById/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("contentAsString = " + contentAsString);

    }
}
