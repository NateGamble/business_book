package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepo;
    @InjectMocks
    UserService userService;
    List<User> standardUsers;
    User eric;

    @Before
    public void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericsreim@email");
        eric.setFirstName("Eric");
        eric.setLastName("Newman");
        eric.setUsername("enewman11");
        eric.setUserId(1);
        eric.setRole(Role.USER);
        eric.setPassword("Packers1");
        eric.setPhoneNumber("123");
        MockitoAnnotations.initMocks(this);
        standardUsers = new ArrayList<>();
        standardUsers.add(eric);

    }

    @Test
    public void getAllUsers() {
        when(userRepo.findAll()).thenReturn(standardUsers);

        List<User> users = userService.getAllUsers();
        // System.out.println(users);
        assertEquals(standardUsers, users);
    }

}
