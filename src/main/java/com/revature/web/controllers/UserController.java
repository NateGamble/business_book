package com.revature.web.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.dtos.Principal;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.JwtParser;
import com.revature.util.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @Secured(allowedRoles = {"Admin"})
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    //@Secured(allowedRoles = {"USER", "OWNER"})
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
        return null; //return userService.getUserByEmail(email);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        userService.register(user);
    }

    @PutMapping()
    public void updateUser(@RequestBody User user) {
        userService.updateProfile(user);
    }


    // How do we want to handle a delete?
    @DeleteMapping(path = "delete/id/{id}")
    public void deleteUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        userService.delete(user);
    }
}
