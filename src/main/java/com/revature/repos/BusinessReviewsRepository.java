package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.Review;
import com.revature.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessReviewsRepository extends CrudRepository<Review, Integer> {
    
    List<Review> findReviewsByBusiness(Business bus);
    List<Review> findReviewsByUser(User u);

}