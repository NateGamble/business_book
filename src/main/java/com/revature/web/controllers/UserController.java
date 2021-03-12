package com.revature.web.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.dtos.Principal;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.JwtParser;
import com.revature.util.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Controller Class that handles any HttpRequest for a {@link User}
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * The UserService object that handles data validation and Repository calls
     */
    private final UserService userService;

    /**
     * The JwtParser that handles parsing the JWT to obtain user data
     */
    private final JwtParser jwtParser;


    /**
     * UserController all args constructor
     * @param userService the UserService given by Spring
     * @param jwtParser the JwtParser given by Spring
     */
    @Autowired
    public UserController (UserService userService, JwtParser jwtParser) {
        this.userService = userService;
        this.jwtParser = jwtParser;
    }

    /**
     * Handles an HTTPRequest for getting all users
     * @return a List of all Users
     */
    @Secured(allowedRoles = "ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //@Secured(allowedRoles = {"Admin"})
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Handles an HTTPRequest for getting the logged in user.
     * @param req the HTTPRequest
     * @return the current User in object form
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrentUser(HttpServletRequest req) {

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
        return userService.getUserByUsername(principal.getUsername());
    }

    /**
     * Handles an HTTPRequest for getting a User by their id
     * @param id the id value for the User
     * @return a User object
     */
    @Secured(allowedRoles = "ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    /**
     * Handles an HTTPRequest for getting a User by their username
     * @param username the username of the desired User
     * @return a User object
     */
    @Secured(allowedRoles = "ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Handles an HTTPRequest for getting the User by their email
     * @param email the email of the desired User
     * @return a User object
     */
    @Secured(allowedRoles = "ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Handles an HTTPRequest for creating a new User
     * @param user the user to create
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user) {
        userService.register(user);
    }

    /**
     * Handles an HTTPRequest for updating a User. Expects a userId value if
     * an update is desired, and no userId value if an insert is desired.
     * @param user the user to update
     * @param resp the HttpServletResponse object
     */
    @PutMapping()
    public void updateUser(@RequestBody User user, HttpServletResponse resp) {
        if (user.getUserId() == null) {
            resp.setStatus(201);
            userService.register(user);
        } else {
            resp.setStatus(204);
            userService.updateProfile(user);
        }
    }


    /**
     * Handles an HTTPRequest for deleting a User
     * @param id the id value of the User to delete
     */
    // How do we want to handle a delete?
    @Secured(allowedRoles = "ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/id/{id}")
    public void deleteUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        userService.delete(user);
    }
}
