package com.revature.web.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.dtos.Principal;
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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtParser jwtParser;

    @Autowired
    public UserController (UserService userService, JwtParser jwtParser) {
        this.userService = userService;
        this.jwtParser = jwtParser;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //@Secured(allowedRoles = {"Admin"})
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(allowedRoles = {"User", "Owner"})
    public User getCurrentUser(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String token = "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("bb-token")) {
                token = cookie.getValue();
            }
        }

        Principal principal = jwtParser.parseToken(token);

        return userService.getUserByUsername(principal.getUsername());
    }

    @GetMapping(path = "/id/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping(path = "/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user) {
        userService.register(user);
    }

    @PutMapping()
    public void updateUser(@RequestBody User user, HttpServletResponse resp) {
        boolean updated = userService.updateProfile(user);
        if (updated) {
            resp.setStatus(204);
        } else {
            resp.setStatus(200);
        }
    }


    // How do we want to handle a delete?
    @DeleteMapping(path = "delete/id/{id}")
    public void deleteUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        userService.delete(user);
    }
}
