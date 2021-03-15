package com.revature.web.controllers;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.models.Post;
import com.revature.models.Review;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.BusinessRepository;
import com.revature.repos.HoursRepository;
import com.revature.repos.PostRepository;
import com.revature.repos.ReviewsRepository;

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

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BusinessControllerIntegrationTest {


    private MockMvc mockMvc;
    private WebApplicationContext webContext;
    @MockBean
    private BusinessRepository businessRepoMock;
    @MockBean
    private PostRepository postRepoMock;
    @MockBean
    private HoursRepository hoursRepoMock;
    @MockBean
    private ReviewsRepository reviewRepoMock;
    

    Business fullBusiness, minBusiness;
    User fullUser;
    List<Business> list;

    @Autowired
    public BusinessControllerIntegrationTest(WebApplicationContext webContext) {
        this.webContext = webContext;
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        // set up data for repos to return so we don't interact w/ database
        fullUser = new User();
        fullUser.setUserId(1);
        fullUser.setUsername("ngamble");
        fullUser.setPassword("pass");
        fullUser.setEmail("nathan.fake@email.gov");
        fullUser.setPhoneNumber("555-555-5555");
        fullUser.setFirstName("Nate");
        fullUser.setLastName("Gamble");
        fullUser.setRegisterDatetime(now);
        fullUser.setActive(true);
        fullUser.setRole(Role.ADMIN);

        // Start business setup
		fullBusiness = new Business();
        fullBusiness.setId(1);
        fullBusiness.setBusinessName("Aldi");
        fullBusiness.setBusinessType("Store");
        fullBusiness.setEmail("email@business.com");
        fullBusiness.setLocation("here");
        fullBusiness.setActive(true);
        fullBusiness.setOwner(fullUser);
        fullBusiness.setRegisterDatetime(now);

        // review for business
        Review review = new Review();
        review.setId(1);
        review.setBusiness(fullBusiness);
        review.setRating(5.0);
        review.setReview("best business evah!");
        review.setUser(fullUser);

        // hours for business
        Hours hours = new Hours();
        hours.setHoursId(1);
        hours.setBusiness(fullBusiness);
        hours.setDay(0);
        // yyyy-[m]m-[d]d hh:mm:ss
        hours.setOpen(Timestamp.valueOf("2020-3-14 08:00:00"));
        hours.setClosed(Timestamp.valueOf("2020-3-14 21:00:00"));

        // post for business
        Post post = new Post();
        post.setPostId(1);
        post.setBusiness(fullBusiness);
        post.setCreatedTime(now);
        post.setPostType("Sale");
        post.setBody("We're having a sale on all watermelon because they're gross");

        // Add review, hours and post to full business
        fullBusiness.setReviews(List.of(review));
        fullBusiness.setHours(List.of(hours));
        fullBusiness.setPosts(List.of(post));

        Business minBusiness = new Business();
        minBusiness.setId(2);
        minBusiness.setBusinessName("Fake name");
        minBusiness.setEmail("fake email");
        minBusiness.setBusinessType("petshop");
        
        fullUser.setFavorites(List.of(minBusiness));

        list = List.of(fullBusiness, minBusiness);
    }

    @AfterAll
    public static void printFinished() {
        System.out.println("All tests finished");
    }

    @Test
    public void test_getBusinessById_givenValidId() throws Exception {
        when(businessRepoMock.findById(minBusiness.getId())).thenReturn(Optional.of(minBusiness));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/businesses/id/{id}", minBusiness.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(minBusiness.getId()))
                    .andExpect(jsonPath("$.businessName").value(minBusiness.getBusinessName()))
                    .andExpect(jsonPath("$.email").value(minBusiness.getEmail()))
                    .andExpect(jsonPath("$.businessType").value(minBusiness.getBusinessType()));
    }

    @Test
    public void test_getUserById_givenInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getAllUsers() throws Exception {

        when(userRepoMock.findAll()).thenReturn(list);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.size()").value(list.size()));
    }

    @Test
    public void test_getUserByEmail_givenValidEmail() throws Exception {
        when(userRepoMock.findUserByEmail(minUser.getEmail())).thenReturn(Optional.of(minUser));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", minUser.getEmail()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.userId").value(minUser.getUserId()))
                    .andExpect(jsonPath("$.username").value(minUser.getUsername()))
                    .andExpect(jsonPath("$.password").value(minUser.getPassword()))
                    .andExpect(jsonPath("$.firstName").value(minUser.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(minUser.getLastName()))
                    .andExpect(jsonPath("$.email").value(minUser.getEmail()));
    }

    @Test
    public void test_getUserByEmail_givenInvalidEmail() throws Exception {
        when(userRepoMock.findUserByEmail("fakeEmail@gmail.com")).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", "fakeEmail@gmail.abc"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void test_getUserByUsername_givenValidUsername() throws Exception {
        when(userRepoMock.findUserByUsername(fullUser.getUsername())).thenReturn(Optional.of(fullUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/username/{username}", fullUser.getUsername()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.userId").value(fullUser.getUserId()))
                    .andExpect(jsonPath("$.username").value(fullUser.getUsername()))
                    .andExpect(jsonPath("$.password").value(fullUser.getPassword()))
                    .andExpect(jsonPath("$.firstName").value(fullUser.getFirstName()))
                    .andExpect(jsonPath("$.lastName").value(fullUser.getLastName()))
                    .andExpect(jsonPath("$.email").value(fullUser.getEmail()))
                    .andExpect(jsonPath("$.active").value(fullUser.isActive()))
                    .andExpect(jsonPath("$.phoneNumber").value(fullUser.getPhoneNumber()))
                    .andExpect(jsonPath("$.role").value(fullUser.getRole().toString().toUpperCase()));
    }

    @Test
    public void test_getUserByUsername_givenInvalidUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/username/{username}", "fake!!"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void test_createNewUser_givenValidUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("pass");
        newUser.setFirstName("first");
        newUser.setLastName("last");
        newUser.setEmail("email@mail.com");
        newUser.setRole(Role.USER);
        when(userRepoMock.save(newUser)).thenReturn(null);

        String newUserJson = "{" +
            "\"username\":\"" + newUser.getUsername() + "\", " + 
            "\"password\":\"" + newUser.getPassword() + "\", " + 
            "\"firstName\":\"" + newUser.getFirstName() + "\", " + 
            "\"lastName\":\"" + newUser.getLastName() + "\", " + 
            "\"email\":\"" + newUser.getEmail() + "\", " + 
            "\"role\":\"" + newUser.getRole().toString().toUpperCase() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newUserJson))
            .andExpect(status().isCreated());
    }

    @Test
    public void test_createNewUser_givenInvalidUser() throws Exception {
        User newUser = new User();
        newUser.setPassword("pass");
        newUser.setFirstName("first");
        newUser.setLastName("last");
        newUser.setEmail("email@mail.com");
        when(userRepoMock.save(newUser)).thenReturn(null);

        String newUserJson = "{" +
            "\"password\":\"" + newUser.getPassword() + "\", " + 
            "\"firstName\":\"" + newUser.getFirstName() + "\", " + 
            "\"lastName\":\"" + newUser.getLastName() + "\", " + 
            "\"email\":\"" + newUser.getEmail() + "\"" +
            "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newUserJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate_onValidUpdatedUser() throws Exception {
        minUser.setRole(Role.ADMIN);
        when(userRepoMock.save(minUser)).thenReturn(null);

        String minUserJson = "{" +
            "\"userId\":\"" + minUser.getUserId() + "\", " + 
            "\"username\":\"" + minUser.getUsername() + "\", " + 
            "\"password\":\"" + minUser.getPassword() + "\", " + 
            "\"firstName\":\"" + minUser.getFirstName() + "\", " + 
            "\"lastName\":\"" + minUser.getLastName() + "\", " + 
            "\"email\":\"" + minUser.getEmail() + "\", " + 
            "\"role\":\"" + minUser.getRole().toString().toUpperCase() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(minUserJson))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdate_onValidNewUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("pass");
        newUser.setFirstName("first");
        newUser.setLastName("last");
        newUser.setEmail("email@mail.com");
        newUser.setRole(Role.USER);
        when(userRepoMock.save(newUser)).thenReturn(null);

        String newUserJson = "{" +
            "\"username\":\"" + newUser.getUsername() + "\", " + 
            "\"password\":\"" + newUser.getPassword() + "\", " + 
            "\"firstName\":\"" + newUser.getFirstName() + "\", " + 
            "\"lastName\":\"" + newUser.getLastName() + "\", " + 
            "\"email\":\"" + newUser.getEmail() + "\", " + 
            "\"role\":\"" + newUser.getRole().toString().toUpperCase() + "\"" + 
            "}";

        // expected 201, is 204
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newUserJson))
            .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate_withInvalidUser() throws Exception {
        minUser.setRole(Role.ADMIN);
        when(userRepoMock.save(minUser)).thenReturn(null);
        when(userRepoMock.findUserByUsername(fullUser.getUsername())).thenReturn(Optional.of(fullUser));

        String minUserJson = "{" +
            "\"userId\":\"" + minUser.getUserId() + "\", " + 
            "\"username\":\"" + fullUser.getUsername() + "\", " + 
            "\"password\":\"" + minUser.getPassword() + "\", " + 
            "\"firstName\":\"" + minUser.getFirstName() + "\", " + 
            "\"lastName\":\"" + minUser.getLastName() + "\", " + 
            "\"email\":\"" + minUser.getEmail() + "\", " + 
            "\"role\":\"" + minUser.getRole().toString().toUpperCase() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(minUserJson))
            .andExpect(status().isConflict());
    }

    @Test
    public void testDelete_withValidUserId() throws Exception {
        when(userRepoMock.findById(fullUser.getUserId())).thenReturn(Optional.of(fullUser));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", fullUser.getUserId()))
            .andExpect(status().isOk());
    }

    @Test
    public void testDelete_withInvalidUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", -1))
            .andExpect(status().isBadRequest());
    }
    
}
