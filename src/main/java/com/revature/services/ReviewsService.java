package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.*;
import com.revature.repos.ReviewsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles {@link Review} and uses a {@link ReviewsRepository}
 * to interact with the database
 */
@Service
public class ReviewsService {

    /**
     * ReviewsRepository that is managed by Spring
     */
    private ReviewsRepository repo;

    /**
     * Constructor for ReviewsService
     * @param repo ReviewsRepository from Spring
     */
    @Autowired
    public ReviewsService(ReviewsRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets a singular Review by id
     * @param id the id of the Review to find
     * @return an Optional Review object with the desired id
     */
    public Optional<Review> findReviewByReviewId(int id) {
        return repo.findById(id);
    }

    /**
     * Gets the Reviews for a corresponding Business
     * @param bus the Business to look for Reviews on
     * @return a List of Review objects with the desired Business
     */
    public List<Review> findReviewsByBusiness(Business bus) {
        return repo.findReviewsByBusiness(bus);
    }

    /**
     * Gets the Reviews written by a specific User
     * @param u the User that wrote the Reviews
     * @return a List of Review objects from the desired User
     */
    public List<Review> findReviewsByUser(User u) {
        return repo.findReviewsByUser(u);
    }

    /**
     * Adds a Review to the database
     * @param review the Review to add
     */
    public void createReview(Review review) {
        if (!isReviewValid(review))
            throw new ResourcePersistenceException();
        
        repo.save(review);
    }

    /**
     * Update a Review object
     * @param review the Review to be updated
     */
    public void editReview(Review review) {
        if (!isReviewValid(review))
            throw new ResourcePersistenceException();
        
        repo.save(review);
    }

    /**
     * Deletes a Review
     * @param review the Review object to delete
     */
    public void deleteReview(Review review) {
        if (!isReviewValid(review))
            throw new InvalidRequestException();

        repo.delete(review);
    }


    /**
     * Checks the Review object's field for validity
     * @param review the Review to check
     * @return a boolen if the Review is valid or not
     */
    protected boolean isReviewValid(Review review) {
        if (review.getBusiness() == null) return false;
        if (review.getUser() == null) return false;
        if (review.getRating() == null) return false;

        return true;
    }
    
}
