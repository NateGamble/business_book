package com.revature.web.controllers;

import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.models.Review;
import com.revature.models.Post;
import com.revature.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessService bizService;

    @Autowired
    public BusinessController (BusinessService bizService) { this.bizService = bizService; }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Business> getAllBusinesses() { return bizService.getAllBusinesses(); }

    @GetMapping(path = "/id/{id}")
    public Business getBusinessById(@PathVariable int id) { return bizService.getBusinessById(id); }

    @GetMapping(path = "/name/{businessName}")
    public Business getBusinessByName(@PathVariable String businessName) { return bizService.getBusinessByBusinessName(businessName); }

    @GetMapping(path = "/email/{email}")
    public Business getBusinessByEmail(@PathVariable String email) { return bizService.getBusinessByEmail(email); }

    @GetMapping(path = "/location/{location}")
    public Business getBusinessByLocation(@PathVariable String location) { return bizService.getBusinessByLocation(location); }

    @GetMapping(path = "/registrationDate/{date}")
    public Business getBusinessByRegistrationDate(@PathVariable Timestamp date) { return bizService.getBusinessByRegistrationDate(date); }

    @GetMapping(path = "/reviews/{id}")
    public List<Review> getBusinessReviews(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return biz.getReviews();
    }

    @GetMapping(path = "/hours/{id}")
    public List<Hours> getBusinessHours(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return biz.getHours();
    }

    @GetMapping(path = "/posts/{id}")
    public List<Post> getBusinessPosts(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return biz.getPosts();
    }
}
