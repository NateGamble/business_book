package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.*;
import com.revature.repos.BusinessReviewsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessReviewsService {

    private BusinessReviewsRepository repo;

    @Autowired
    public BusinessReviewsService(BusinessReviewsRepository repo) {
        this.repo = repo;
    }

    public Optional<BusinessReviews> findReviewByReviewId(int id) {
        return repo.findById(id);
    }

    public List<BusinessReviews> findReviewsByBusiness(Business bus) {
        return repo.findReviewsByBusiness(bus);
    }

    public List<BusinessReviews> findReviewsByUser(User u) {
        return repo.findReviewsByUser(u);
    }

    public void createReview(BusinessReviews review) {
        if (!isReviewValid(review))
            throw new ResourcePersistenceException();
        
        repo.save(review);
    }

    public void editReview(BusinessReviews review) {
        if (!isReviewValid(review))
            throw new ResourcePersistenceException();
        
        repo.save(review);
    }

    public void deleteReview(BusinessReviews review) {
        if (!isReviewValid(review))
            throw new InvalidRequestException();

        repo.delete(review);
    }


    protected boolean isReviewValid(BusinessReviews review) {
        if (review.getBusiness() == null) return false;
        if (review.getUser() == null) return false;
        if (review.getRating() == null) return false;

        return true;
    }
    
}
