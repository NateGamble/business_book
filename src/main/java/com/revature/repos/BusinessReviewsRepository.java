package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.BusinessReviews;
import com.revature.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessReviewsRepository extends CrudRepository<BusinessReviews, Integer> {
    
    List<BusinessReviews> findReviewsByBusiness(Business bus);
    List<BusinessReviews> findReviewsByUser(User u);

}