package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    private void initdata(){

        dob = LocalDate.of(1990,1,1);
        request = UserCreationRequest.builder()
                .username("john")
                .firstname("heh")
                .lastname("Doe")
                .password("1234567")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("06f")
                .username("john")
                .firstname("heh")
                .lastname("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser(){
        //given


        //when



        //then
    }

}
