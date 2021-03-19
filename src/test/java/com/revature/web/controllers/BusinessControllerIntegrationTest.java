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
    Review review;
    Post post;
    Hours hours;
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
        review = new Review();
        review.setId(1);
        review.setBusiness(fullBusiness);
        review.setRating(5.0);
        review.setReview("best business evah!");
        review.setUser(fullUser);

        // hours for business
        hours = new Hours();
        hours.setHoursId(1);
        hours.setBusiness(fullBusiness);
        hours.setDay(0);
        // yyyy-[m]m-[d]d hh:mm:ss
        hours.setOpen(Timestamp.valueOf("2020-3-14 08:00:00"));
        hours.setClosed(Timestamp.valueOf("2020-3-14 21:00:00"));

        // post for business
        post = new Post();
        post.setPostId(1);
        post.setBusiness(fullBusiness);
        post.setCreatedTime(now);
        post.setPostType("Sale");
        post.setBody("We're having a sale on all watermelon because they're gross");

        // Add review, hours and post to full business
        fullBusiness.setReviews(List.of(review));
        fullBusiness.setHours(List.of(hours));
        fullBusiness.setPosts(List.of(post));

        minBusiness = new Business();
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
    public void test_getBusinessById_givenInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/businesses/id/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getAllBusinesses() throws Exception {

        when(businessRepoMock.findAll()).thenReturn(list);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/businesses"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.size()").value(list.size()));
    }

    @Test
    public void test_getBusinessByEmail_givenValidEmail() throws Exception {
        when(businessRepoMock.findBusinessByEmail(minBusiness.getEmail())).thenReturn(Optional.of(minBusiness));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/businesses/email/{email}", minBusiness.getEmail()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(minBusiness.getId()))
                    .andExpect(jsonPath("$.businessName").value(minBusiness.getBusinessName()))
                    .andExpect(jsonPath("$.email").value(minBusiness.getEmail()))
                    .andExpect(jsonPath("$.businessType").value(minBusiness.getBusinessType()));
    }

    @Test
    public void test_getBusinessByEmail_givenInvalidEmail() throws Exception {
        when(businessRepoMock.findBusinessByEmail("fakeEmail@gmail.com")).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/businesses/email/{email}", "fakeEmail@gmail.abc"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void test_getBusinessByName_givenValidName() throws Exception {
        when(businessRepoMock.findBusinessByBusinessName(fullBusiness.getBusinessName())).thenReturn(Optional.of(fullBusiness));

        mockMvc.perform(MockMvcRequestBuilders.get("/businesses/name/{name}", fullBusiness.getBusinessName()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").value(fullBusiness.getId()))
                    .andExpect(jsonPath("$.businessName").value(fullBusiness.getBusinessName()))
                    .andExpect(jsonPath("$.email").value(fullBusiness.getEmail()))
                    .andExpect(jsonPath("$.businessType").value(fullBusiness.getBusinessType()))
                    .andExpect(jsonPath("$.location").value(fullBusiness.getLocation()))
                    .andExpect(jsonPath("$.active").value(fullBusiness.isActive()));
    }

    @Test
    public void test_getBusinessByName_givenInvalidName() throws Exception {
        when(businessRepoMock.findBusinessByBusinessName("fake!!")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/businesses/name/{name}", "fake!!"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test @Ignore
    public void test_createNewBusiness_givenValidBusiness() throws Exception {
        Business newBusiness = new Business();
        minBusiness.setBusinessName("new business");
        minBusiness.setEmail("new email");
        minBusiness.setBusinessType("Newsstand");

        when(businessRepoMock.save(newBusiness)).thenReturn(null);

        String newBusinessJson = "{" +
            "\"businessName\":\"" + newBusiness.getBusinessName() + "\", " + 
            "\"email\":\"" + newBusiness.getEmail() + "\", " + 
            "\"businessType\":\"" + newBusiness.getBusinessType() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/businesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newBusinessJson))
            .andExpect(status().isCreated());
    }

    @Test @Ignore
    public void test_createNewBusiness_givenInvalidBusiness() throws Exception {
        Business newBusiness = new Business();
        newBusiness.setEmail("new email");
        when(businessRepoMock.save(newBusiness)).thenReturn(null);

        String newBusinessJson = "{" +
            "\"email\":\"" + newBusiness.getEmail() + "\"" +
            "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/businesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newBusinessJson))
            .andExpect(status().isBadRequest());
    }

    @Test @Ignore
    public void testUpdate_onValidUpdatedBusiness() throws Exception {
        minBusiness.setBusinessName("new business");
        when(businessRepoMock.save(minBusiness)).thenReturn(null);

        String minBusinessJson = "{" +
            "\"businessName\":\"" + minBusiness.getBusinessName() + "\", " + 
            "\"email\":\"" + minBusiness.getEmail() + "\", " + 
            "\"businessType\":\"" + minBusiness.getBusinessType() + "\"" + 
            "}";

        // getting null pointer here
        mockMvc.perform(MockMvcRequestBuilders.put("/businesses")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(minBusinessJson))
                .andExpect(status().isNoContent());
    }

    @Test @Ignore
    public void testUpdate_onValidNewBusiness() throws Exception {
        Business newBusiness = new Business();
        newBusiness.setBusinessName("new business");
        newBusiness.setEmail("new email");
        newBusiness.setBusinessType("Newsstand");

        when(businessRepoMock.save(newBusiness)).thenReturn(null);

        String newBusinessJson = "{" +
            "\"businessName\":\"" + newBusiness.getBusinessName() + "\", " + 
            "\"email\":\"" + newBusiness.getEmail() + "\", " + 
            "\"businessType\":\"" + newBusiness.getBusinessType() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/businesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newBusinessJson))
            .andExpect(status().isCreated());
    }

    @Test @Ignore
    public void testUpdate_withInvalidBusiness() throws Exception {
        minBusiness.setEmail(fullBusiness.getEmail());
        when(businessRepoMock.save(minBusiness)).thenReturn(null);
        when(businessRepoMock.findBusinessByEmail(minBusiness.getEmail())).thenReturn(Optional.of(fullBusiness));

        

        String minBusinessJson = "{" +
            "\"businessName\":\"" + minBusiness.getBusinessName() + "\", " + 
            "\"email\":\"" + minBusiness.getEmail() + "\", " + 
            "\"businessType\":\"" + minBusiness.getBusinessType() + "\"" + 
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/businesses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(minBusinessJson))
            .andExpect(status().isConflict());
    }

    @Test
    public void testDelete_withValidBusinessId() throws Exception {
        when(businessRepoMock.findById(fullBusiness.getId())).thenReturn(Optional.of(fullBusiness));

        mockMvc.perform(MockMvcRequestBuilders.delete("/businesses/id/{id}", fullBusiness.getId()))
            .andExpect(status().isOk());
    }

    @Test
    public void testDelete_withInvalidBusinessId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/businesses/id/{id}", -1))
            .andExpect(status().isBadRequest());
    }
    
}
