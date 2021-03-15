package com.revature.web.controllers;

import com.revature.dtos.Principal;
import com.revature.models.*;
import com.revature.services.*;
import com.revature.util.JwtParser;
import com.revature.util.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller Class to handle all HTTPRequests for {@link Business}, {@link Post}, and {@link Review}
 */
@RestController
@RequestMapping("/businesses")
public class BusinessController {

    /**
     * Service class for {@link Business}
     */
    private final BusinessService bizService;

    /**
     * Service class for {@link Review}
     */
    private final ReviewsService reviewsService;

    /**
     * Service class for {@link Post}
     */
    private final PostService postService;

    /**
     * Service class for {@link Hours}
     */
    private final HoursService hoursService;

    private final UserService userService;

    private final JwtParser jwtParser;

    /**
     * All-args constructor
     * @param bizService service class for Business
     * @param reviewsService service class for Review
     * @param postService service class for Post
     * @param hoursService service class for Hours
     */
    @Autowired
    public BusinessController (BusinessService bizService, ReviewsService reviewsService,
                               PostService postService, HoursService hoursService, UserService userService,
                               JwtParser jwtParser) {
        this.bizService = bizService;
        this.reviewsService = reviewsService;
        this.postService = postService;
        this.hoursService = hoursService;
        this.userService = userService;
        this.jwtParser = jwtParser;
    }

    // ADMIN PRIVILEGES SECTION

    /**
     * Handles an HTTPRequest for getting all Businesses
     * @return a List of all Businesses
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Business> getAllBusinesses() { return bizService.getAllBusinesses(); }

    /**
     * Handles an HTTPRequest for getting a Business by Id
     * @param id the id of the desired Business
     * @return the Business object that corresponds to the given Id
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id/{id}")
    @Secured(allowedRoles = {"ADMIN"})
    public Business getBusinessById(@PathVariable int id) { return bizService.getBusinessById(id); }

    /**
     * Handles an HTTPRequest for deleting a Review by Id
     * @param id the id of the desired Review to delete
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/reviews/{id}")
    @Secured(allowedRoles = {"ADMIN"})
    public void deleteBusinessReviewById(@PathVariable int id) {
        Review review = reviewsService.findReviewByReviewId(id).get();
        reviewsService.deleteReview(review);
    }

    /**
     * Handles an HTTPRequest for deleting a Business by Id
     * @param id the id of the Business to delete
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/id/{id}")
    @Secured(allowedRoles = {"ADMIN", "OWNER"})
    public void deleteBusinessById(@PathVariable int id) {
        bizService.deleteBusinessById(id);
    }

    /**
     * Handles an HTTPRequest for creating a Business
     * @param biz the Business object to create
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @Secured(allowedRoles = {"ADMIN", "OWNER"})
    public void addNewBusiness(@RequestBody Business biz, HttpServletRequest req) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        biz.setRegisterDatetime(now);

        // Get the cookies (JWT) from the request
        Cookie[] cookies = req.getCookies();
        String token = "";

        // Loop through cookies to find the correct one in case there are multiple cookies
        for (Cookie cookie : cookies) {

            // Finding the JWT cookie
            if (cookie.getName().equals("bb-token")) {
                token = cookie.getValue();
            }
        }

        // Create a Principal from the JWT
        Principal principal = jwtParser.parseToken(token);

        // Return the User found by the username from the Principal
        User user = userService.getUserByUsername(principal.getUsername());

        biz.setOwner(user);

        bizService.addBusiness(biz);
    }

    /**
     * Handles an HTTPRequest for updating (PUT) a Business
     * @param biz the Business object
     * @param resp the HTTPServletResponse
     */
    @PutMapping()
    @Secured(allowedRoles = {"ADMIN", "OWNER"})
    public void updateBusiness(@RequestBody Business biz, HttpServletResponse resp) {
        if (biz.getId() == null || biz.getId() == 0) {
            resp.setStatus(202);
            bizService.addBusiness(biz);
        } else {
            resp.setStatus(204);
            bizService.updateBusiness(biz);
        }
    }

    // END ADMIN PRIVILEGES SECTION ///////////////////////////////////////////////////

    // OWNER PRIVILEGES SECTION


    // Should be split into a POST and PUT???

    /**
     * Handles an HTTPRequest for creating a Post
     * @param id the Business Id for the Post
     * @param post the Post object to add
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/id/{id}/posts")
    @Secured(allowedRoles = {"OWNER"})
    public void addNewBusinessPost(@PathVariable int id, @RequestBody Post post) {
        Business biz = bizService.getBusinessById(id);
        post.setBusiness(biz);

        postService.createPost(post);
    }

    /**
     * Handles an HTTPRequest to update (PUT) a Post
     * @param id the Id of the Business that the post is associated with
     * @param post the Post object
     * @param resp the HTTPServlet response
     */
    @PutMapping(path = "/id/{id}/posts")
    @Secured(allowedRoles = {"OWNER"})
    public void updateBusinessPost(@PathVariable int id, @RequestBody Post post, HttpServletResponse resp) {
        Business biz = bizService.getBusinessById(id);
        post.setBusiness(biz);

        if (post.getPostId() == null || post.getPostId() ==  0) {
            resp.setStatus(202);
            postService.createPost(post);
        } else {
            resp.setStatus(204);
            postService.editPost(post);
        }
    }

    // END OWNER PRIVILEGES SECTION

    /**
     * Handles an HTTPRequest for getting Business by name
     * @param businessName the name of the Business
     * @return the Business object
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/name/{businessName}")
    public Business getBusinessByName(@PathVariable String businessName) { return bizService.getBusinessByBusinessName(businessName); }

    /**
     * Handles an HTTPRequest for getting a Business by email
     * @param email the email of the Business desired
     * @return a Business object
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/email/{email}")
    public Business getBusinessByEmail(@PathVariable String email) { return bizService.getBusinessByEmail(email); }

    /**
     * Handles an HTTPRequest for getting a Business by location
     * @param location the String address of a Business
     * @return a Business object
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/location/{location}")
    public Business getBusinessByLocation(@PathVariable String location) { return bizService.getBusinessByLocation(location); }

    // Is this needed?
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/registrationDate/{date}")
    public Business getBusinessByRegistrationDate(@PathVariable Timestamp date) { return bizService.getBusinessByRegistrationDate(date); }

    /**
     * Handles an HTTPRequest for getting Businesses by type
     * @param type the type of the Businesses desired
     * @return a List of Business objects
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/type/{type}")
    public List<Business> getBusinessesByType(@PathVariable String type) {
        return bizService.getBusinessesByType(type);
    }

    /**
     * Handles an HTTPRequest for getting the Reviews of a Business
     * @param id the Id of the Business
     * @return a List of Review objects
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id/{id}/reviews")
    public List<Review> getBusinessReviews(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return reviewsService.findReviewsByBusiness(biz);
    }

    /**
     * Handles an HTTPRequest for getting the Hours of a Business
     * @param id the Id of the Business
     * @return a List of Hours
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id/{id}/hours")
    public List<Hours> getBusinessHours(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return hoursService.findHoursByBusiness(biz);
    }

    /**
     * Handles an HTTPRequest for getting the Posts for a Business
     * @param id the id value of the business
     * @return a List of Posts
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id/{id}/posts")
    public List<Post> getBusinessPosts(@PathVariable int id) {
        Business biz =  bizService.getBusinessById(id);
        return postService.findPostsByBusiness(biz);
    }

    /**
     * Handles an HTTPRequest for creating a Review
     * @param review the review given by the User
     * @param id the Business Id to review
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/id/{id}/reviews")
    @Secured(allowedRoles = {"USER"})
    public void addNewBusinessReview (@RequestBody Review review, @PathVariable int id) {
        Business biz = bizService.getBusinessById(id);
        review.setBusiness(biz);
        reviewsService.createReview(review);
    }

    /**
     * Handles an HTTPRequest for updating (PUT) a Review
     * @param review the review to be updated
     * @param id the Business Id for the Review
     * @param resp the HTTPServletResponse object
     */
    @PutMapping(path = "/id/{id}/reviews")
    @Secured(allowedRoles = {"USER"})
    public void updateBusinessReview (@RequestBody Review review, @PathVariable int id, HttpServletResponse resp) {
        Business biz = bizService.getBusinessById(id);
        review.setBusiness(biz);

        if (review.getId() == null || review.getId() == 0) {
            resp.setStatus(202);
            reviewsService.createReview(review);
        } else {
            resp.setStatus(204);
            reviewsService.editReview(review);
        }
    }
}
