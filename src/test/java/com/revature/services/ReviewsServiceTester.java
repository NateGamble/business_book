package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.Business;
import com.revature.models.Review;
import com.revature.models.User;

import com.revature.repos.ReviewsRepository;
import org.jeasy.random.EasyRandom;
import com.revature.repos.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import java.sql.Timestamp;


public class ReviewsServiceTester {
    //injectmocks actually creates the mock and also injects the dependent mocks that are marked with @mock (userrepo here)
    @InjectMocks
    ReviewsService revService;

    @Mock
    ReviewsRepository revRepo;

    Business bus;
    Review validReview, invalidReview;
    User user;
    List<Review> reviewList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setEmail("nathan.gamble@revature.net");
        user.setUsername("ngamble");
        user.setPassword("password");
        bus = new Business();
        bus.setBusinessName("fake");
        bus.setBusinessType("pets");
        bus.setEmail("fake.email@imreviewor.gov");
        bus.setLocation("123 Cherry Wood Ln");
        bus.setOwner(user);
        validReview = new Review();
        validReview.setId(1);
        validReview.setBusiness(bus);
        validReview.setUser(user);
        validReview.setRating(10.0);
        validReview.setReview("good review");
        invalidReview = new Review();
        invalidReview.setId(2);
        invalidReview.setBusiness(null);
        invalidReview.setUser(user);
        invalidReview.setRating(1.0);
        invalidReview.setReview("bad review");
        reviewList = new ArrayList<>();
        reviewList.add(validReview);
        reviewList.add(invalidReview);
    }

    @Test
    @DisplayName("Verifying getReviewByReviewId() works as expected and pulls a review by id")
    public void testfindReviewByReviewId() {
        when(revRepo.findById(validReview.getId())).thenReturn(Optional.ofNullable(validReview));
        assertEquals(java.util.Optional.of(validReview), revService.findReviewByReviewId(validReview.getId()));
    }

    @Test
    @DisplayName("Verifying findReviewsByBusiness() works as expected and pulls reviews by business name")
    public void testFindReviewsByBusiness() {
        when(revRepo.findReviewsByBusiness(bus)).thenReturn(reviewList);
        assertEquals(reviewList, revService.findReviewsByBusiness(bus));
    }

    @Test
    @DisplayName("Verifying findReviewsByUser() works as expected and pulls reviews by user obj")
    public void testFindReviewsByUser() {
        when(revRepo.findReviewsByUser(user)).thenReturn(reviewList);
        assertEquals(reviewList, revService.findReviewsByUser(user));
    }

    @Test
    @DisplayName("Verifying createReview() works as expected and call repo to create new review")
    public void testCreateReview() {
        revService.createReview(validReview);
        verify(revRepo, times(1)).save(validReview);
    }
    @Test
    @DisplayName("Verifying createReview() throws error when expected given invalid review")
    public void testCreateReviewInvalid() {
        assertThrows(ResourcePersistenceException.class, () -> revService.createReview(invalidReview));
        verify(revRepo, times(0)).save(invalidReview);
    }

    @Test
    @DisplayName("Verifying editReview() works as expected and call repo to create update given review")
    public void testEditReview() {
        revService.editReview(validReview);
        verify(revRepo, times(1)).save(validReview);
    }
    @Test
    @DisplayName("Verifying editReview() throws error when expected given invalid review")
    public void testEditReviewInvalid() {
        assertThrows(ResourcePersistenceException.class, () -> revService.editReview(invalidReview));
        verify(revRepo, times(0)).save(invalidReview);
    }

    @Test
    @DisplayName("Verifying deleteReview() works as expected and call repo to delete given review")
    public void testDeleteReview() {
        revService.deleteReview(validReview);
        verify(revRepo, times(1)).delete(validReview);
    }
    @Test
    @DisplayName("Verifying deleteReview() throws error when expected given invalid review")
    public void testDeleteReviewInvalid() {
        assertThrows(InvalidRequestException.class, () -> revService.deleteReview(invalidReview));
        verify(revRepo, times(0)).delete(invalidReview);
    }

    @Test
    @DisplayName("Verifying error thrown on isReviewValid() returns true for valid entry")
    public void testIsReviewValid() {
        assertTrue(revService.isReviewValid(validReview));
    }

    @Test
    @DisplayName("Verifying error thrown on isHoursValid() for null business or day on hours obj")
    public void testIsReviewValidInvalid() {
        assertFalse(revService.isReviewValid(invalidReview));
        invalidReview.setBusiness(bus);
        invalidReview.setUser(null);
        assertFalse(revService.isReviewValid(invalidReview));
        invalidReview.setUser(user);
        invalidReview.setRating(null);
        assertFalse(revService.isReviewValid(invalidReview));
        invalidReview.setRating(2.0);
        assertTrue(revService.isReviewValid(invalidReview));
    }

}
