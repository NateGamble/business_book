package com.revature.web.controllers;

import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.models.Post;
import com.revature.models.Review;
import com.revature.services.BusinessService;
import com.revature.services.HoursService;
import com.revature.services.PostService;
import com.revature.services.ReviewsService;
import com.revature.util.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessService bizService;
    private final ReviewsService reviewsService;
    private final PostService postService;
    private final HoursService hoursService;

    @Autowired
    public BusinessController (BusinessService bizService, ReviewsService reviewsService,
                               PostService postService, HoursService hoursService) {
        this.bizService = bizService;
        this.reviewsService = reviewsService;
        this.postService = postService;
        this.hoursService = hoursService;
    }

    // ADMIN PRIVILEGES SECTION

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Business> getAllBusinesses() { return bizService.getAllBusinesses(); }

    @GetMapping(path = "/id/{id}")
    //@Secured(allowedRoles = {"Admin"})
    public Business getBusinessById(@PathVariable int id) { return bizService.getBusinessById(id); }

    @DeleteMapping(path = "/reviews/{id}")
    //@Secured(allowedRoles = {"Admin"})
    public void deleteBusinessReviewById(@PathVariable int id) {
        Review review = reviewsService.findReviewByReviewId(id).get();
        reviewsService.deleteReview(review);
    }

    @DeleteMapping(path = "/id/{id}")
    //@Secured(allowedRoles = {"Admin", "Owner"})
    public void deleteBusinessById(@PathVariable int id) {
        bizService.deleteBusinessById(id);
    }

    @PutMapping()
    //@Secured(allowedRoles = {"Admin", "Owner"})
    public void addNewBusiness(@RequestBody Business biz) {
        bizService.addBusiness(biz);
    }

    // END ADMIN PRIVILEGES SECTION ///////////////////////////////////////////////////

    // OWNER PRIVILEGES SECTION

    @PutMapping(path = "/id/{id}/posts")
    //@Secured(allowedRoles = {"Owner"})
    public void addNewBusinessPost(@PathVariable int id, @RequestBody Post post) {
        Business biz = bizService.getBusinessById(id);
        post.setBusiness(biz);
        bizService.addBusiness(biz);
    }

    // END OWNER PRIVILEGES SECTION

    @GetMapping(path = "/name/{businessName}")
    public Business getBusinessByName(@PathVariable String businessName) { return bizService.getBusinessByBusinessName(businessName); }

    @GetMapping(path = "/email/{email}")
    public Business getBusinessByEmail(@PathVariable String email) { return bizService.getBusinessByEmail(email); }

    @GetMapping(path = "/location/{location}")
    public Business getBusinessByLocation(@PathVariable String location) { return bizService.getBusinessByLocation(location); }

    @GetMapping(path = "/registrationDate/{date}")
    public Business getBusinessByRegistrationDate(@PathVariable Timestamp date) { return bizService.getBusinessByRegistrationDate(date); }

    @GetMapping(path = "/type/{type}")
    public List<Business> getBusinessesByType(@PathVariable String type) {
        return bizService.getBusinessesByType(type);
    }

    @GetMapping(path = "/id/{id}/reviews")
    public List<Review> getBusinessReviews(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return reviewsService.findReviewsByBusiness(biz);
    }

    @GetMapping(path = "/id/{id}/hours")
    public List<Hours> getBusinessHours(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return hoursService.findHoursByBusiness(biz);
    }

    @GetMapping(path = "/id/{id}/posts")
    public List<Post> getBusinessPosts(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return postService.findPostsByBusiness(biz);
    }

    @PutMapping(path = "/id/{id}/reviews")
    //@Secured(allowedRoles = {"User"})
    public void addNewBusinessReview (@RequestBody Review review, @PathVariable int id) {
        Business biz = bizService.getBusinessById(id);
        review.setBusiness(biz);
        reviewsService.createReview(review);
    }
}
