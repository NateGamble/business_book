package com.revature.services;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTests {

    @Mock
    UserRepository userRepo;
    @InjectMocks
    UserService userService;
    User eric;

    @BeforeEach
    public void setUp() {
        //set up the user we can try to add to the db
        eric = new User();
        eric.setEmail("ericsreim@email");
        eric.setFirstName("Eric");
        eric.setLastName("Newman");
        eric.setUsername("enewman11");
        eric.setUserId(1);
        eric.setRoleId(Role.USER);
        eric.setPassword("Packers1");
        eric.setPhoneNumber("123");

//        eric2 = new User();
//        eric2.setEmail("eric2@email");
//        eric2.setFirstname("Eric2");
//        eric2.setLastname("Newman2");
//        eric2.setUsername("enewman12");
//        eric2.setUserId(2);
//        eric2.setUserRole(2);
//        eric2.setPassword("Packers2");

        //initialize the userservice that actually does the act of registering and such
        userService = new UserService(userRepo);
    }


    @Test
    @DisplayName("Check registration")
    public void saveAndAuthenticate() {
        userService.register(eric);
//        Reimbursement testReim = reimbService.getReimbByUserId(eric.getUserId()).get(0);
//        assertEquals(reim.getAuthorId(), testReim.getAuthorId(),
//                "User not making it to the database or not coming back from getbyuserid");
    }

}
