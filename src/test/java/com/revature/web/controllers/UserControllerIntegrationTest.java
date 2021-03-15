package com.revature.web.controllers;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.revature.models.Business;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserRepository;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserControllerIntegrationTest {

//    private MockMvc mockMvc;
//    private WebApplicationContext webContext;
//    @MockBean
//    private UserRepository userRepoMock;
//
//    User fullUser, minUser;
//    List<User> list;
//
//    @Autowired
//    public UserControllerIntegrationTest(WebApplicationContext webContext) {
//        this.webContext = webContext;
//    }
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
//
//        // set up users and lists for user repo to return so we don't interact w/ database
//        fullUser = new User();
//        fullUser.setUserId(1);
//        fullUser.setUsername("ngamble");
//        fullUser.setPassword("pass");
//        fullUser.setEmail("nathan.fake@email.gov");
//        fullUser.setPhoneNumber("555-555-5555");
//        fullUser.setFirstName("Nate");
//        fullUser.setLastName("Gamble");
//        fullUser.setRegisterDatetime(Timestamp.valueOf(LocalDateTime.now()));
//        fullUser.setActive(true);
//        fullUser.setRole(Role.ADMIN);
//
//        Business bus = new Business();
//        bus.setBusinessName("Fake name");
//        bus.setEmail("fake email");
//        bus.setBusinessType("petshop");
//
//        fullUser.setFavorites(List.of(bus));
//
//        minUser = new User();
//        minUser.setUserId(2);
//        minUser.setUsername("min");
//        minUser.setPassword("p");
//        minUser.setFirstName("f");
//        minUser.setLastName("l");
//        minUser.setEmail("e@m.com");
//
//        list = List.of(fullUser, minUser);
//    }
//
//    @AfterAll
//    public static void printFinished() {
//        System.out.println("All tests finished");
//    }
//
//    @Test
//    public void test_getUserById_givenValidId() throws Exception {
//        when(userRepoMock.findById(minUser.getUserId())).thenReturn(Optional.of(minUser));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", minUser.getUserId()))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                    .andExpect(jsonPath("$.userId").value(minUser.getUserId()))
//                    .andExpect(jsonPath("$.username").value(minUser.getUsername()))
//                    .andExpect(jsonPath("$.password").value(minUser.getPassword()))
//                    .andExpect(jsonPath("$.firstName").value(minUser.getFirstName()))
//                    .andExpect(jsonPath("$.lastName").value(minUser.getLastName()))
//                    .andExpect(jsonPath("$.email").value(minUser.getEmail()));
//    }
//
//    @Test
//    public void test_getUserById_givenInvalidId() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", -1))
//                    .andDo(print())
//                    .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void test_getAllUsers() throws Exception {
//
//        when(userRepoMock.findAll()).thenReturn(list);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                    .andExpect(jsonPath("$.size()").value(list.size()));
//    }
//
//    @Test
//    public void test_getUserByEmail_givenValidEmail() throws Exception {
//        when(userRepoMock.findUserByEmail(minUser.getEmail())).thenReturn(Optional.of(minUser));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", minUser.getEmail()))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                    .andExpect(jsonPath("$.userId").value(minUser.getUserId()))
//                    .andExpect(jsonPath("$.username").value(minUser.getUsername()))
//                    .andExpect(jsonPath("$.password").value(minUser.getPassword()))
//                    .andExpect(jsonPath("$.firstName").value(minUser.getFirstName()))
//                    .andExpect(jsonPath("$.lastName").value(minUser.getLastName()))
//                    .andExpect(jsonPath("$.email").value(minUser.getEmail()));
//    }
//
//    @Test
//    public void test_getUserByEmail_givenInvalidEmail() throws Exception {
//        when(userRepoMock.findUserByEmail("fakeEmail@gmail.com")).thenReturn(Optional.empty());
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", "fakeEmail@gmail.abc"))
//                    .andDo(print())
//                    .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void test_getUserByUsername_givenValidUsername() throws Exception {
//        when(userRepoMock.findUserByUsername(fullUser.getUsername())).thenReturn(Optional.of(fullUser));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/username/{username}", fullUser.getUsername()))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                    .andExpect(jsonPath("$.userId").value(fullUser.getUserId()))
//                    .andExpect(jsonPath("$.username").value(fullUser.getUsername()))
//                    .andExpect(jsonPath("$.password").value(fullUser.getPassword()))
//                    .andExpect(jsonPath("$.firstName").value(fullUser.getFirstName()))
//                    .andExpect(jsonPath("$.lastName").value(fullUser.getLastName()))
//                    .andExpect(jsonPath("$.email").value(fullUser.getEmail()))
//                    .andExpect(jsonPath("$.active").value(fullUser.isActive()))
//                    .andExpect(jsonPath("$.phoneNumber").value(fullUser.getPhoneNumber()))
//                    .andExpect(jsonPath("$.role").value(fullUser.getRole().toString().toUpperCase()));
//    }
//
//    @Test
//    public void test_getUserByUsername_givenInvalidUsername() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/username/{username}", "fake!!"))
//                    .andDo(print())
//                    .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void test_createNewUser_givenValidUser() throws Exception {
//        User newUser = new User();
//        newUser.setUsername("newUser");
//        newUser.setPassword("pass");
//        newUser.setFirstName("first");
//        newUser.setLastName("last");
//        newUser.setEmail("email@mail.com");
//        newUser.setRole(Role.USER);
//        when(userRepoMock.save(newUser)).thenReturn(null);
//
//        String newUserJson = "{" +
//            "\"username\":\"" + newUser.getUsername() + "\", " +
//            "\"password\":\"" + newUser.getPassword() + "\", " +
//            "\"firstName\":\"" + newUser.getFirstName() + "\", " +
//            "\"lastName\":\"" + newUser.getLastName() + "\", " +
//            "\"email\":\"" + newUser.getEmail() + "\", " +
//            "\"role\":\"" + newUser.getRole().toString().toUpperCase() + "\"" +
//            "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(newUserJson))
//            .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void test_createNewUser_givenInvalidUser() throws Exception {
//        User newUser = new User();
//        newUser.setPassword("pass");
//        newUser.setFirstName("first");
//        newUser.setLastName("last");
//        newUser.setEmail("email@mail.com");
//        when(userRepoMock.save(newUser)).thenReturn(null);
//
//        String newUserJson = "{" +
//            "\"password\":\"" + newUser.getPassword() + "\", " +
//            "\"firstName\":\"" + newUser.getFirstName() + "\", " +
//            "\"lastName\":\"" + newUser.getLastName() + "\", " +
//            "\"email\":\"" + newUser.getEmail() + "\"" +
//            "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(newUserJson))
//            .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void testUpdate_onValidUpdatedUser() throws Exception {
//        minUser.setRole(Role.ADMIN);
//        when(userRepoMock.save(minUser)).thenReturn(null);
//
//        String minUserJson = "{" +
//            "\"userId\":\"" + minUser.getUserId() + "\", " +
//            "\"username\":\"" + minUser.getUsername() + "\", " +
//            "\"password\":\"" + minUser.getPassword() + "\", " +
//            "\"firstName\":\"" + minUser.getFirstName() + "\", " +
//            "\"lastName\":\"" + minUser.getLastName() + "\", " +
//            "\"email\":\"" + minUser.getEmail() + "\", " +
//            "\"role\":\"" + minUser.getRole().toString().toUpperCase() + "\"" +
//            "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(minUserJson))
//            .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void testUpdate_onValidNewUser() throws Exception {
//        User newUser = new User();
//        newUser.setUsername("newUser");
//        newUser.setPassword("pass");
//        newUser.setFirstName("first");
//        newUser.setLastName("last");
//        newUser.setEmail("email@mail.com");
//        newUser.setRole(Role.USER);
//        when(userRepoMock.save(newUser)).thenReturn(null);
//
//        String newUserJson = "{" +
//            "\"username\":\"" + newUser.getUsername() + "\", " +
//            "\"password\":\"" + newUser.getPassword() + "\", " +
//            "\"firstName\":\"" + newUser.getFirstName() + "\", " +
//            "\"lastName\":\"" + newUser.getLastName() + "\", " +
//            "\"email\":\"" + newUser.getEmail() + "\", " +
//            "\"role\":\"" + newUser.getRole().toString().toUpperCase() + "\"" +
//            "}";
//
//        // expected 201, is 204
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(newUserJson))
//            .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void testUpdate_withInvalidUser() throws Exception {
//        minUser.setRole(Role.ADMIN);
//        when(userRepoMock.save(minUser)).thenReturn(null);
//        when(userRepoMock.findUserByUsername(fullUser.getUsername())).thenReturn(Optional.of(fullUser));
//
//        String minUserJson = "{" +
//            "\"userId\":\"" + minUser.getUserId() + "\", " +
//            "\"username\":\"" + fullUser.getUsername() + "\", " +
//            "\"password\":\"" + minUser.getPassword() + "\", " +
//            "\"firstName\":\"" + minUser.getFirstName() + "\", " +
//            "\"lastName\":\"" + minUser.getLastName() + "\", " +
//            "\"email\":\"" + minUser.getEmail() + "\", " +
//            "\"role\":\"" + minUser.getRole().toString().toUpperCase() + "\"" +
//            "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(minUserJson))
//            .andExpect(status().isConflict());
//    }
//
//    @Test
//    public void testDelete_withValidUserId() throws Exception {
//        when(userRepoMock.findById(fullUser.getUserId())).thenReturn(Optional.of(fullUser));
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", fullUser.getUserId()))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDelete_withInvalidUserId() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", -1))
//            .andExpect(status().isBadRequest());
//    }

}
