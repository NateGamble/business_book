package com.revature.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.revature.models.Business;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    private WebApplicationContext webContext;

    @Autowired
    public UserControllerIntegrationTest(WebApplicationContext webContext) {
        this.webContext = webContext;
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void test_getUserById_givenValidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    public void test_getUserById_givenInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getAllUsers() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

}
