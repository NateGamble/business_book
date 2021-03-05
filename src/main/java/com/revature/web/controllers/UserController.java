package com.revature.web.controllers;

import com.revature.models.User;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
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

        // Not created yet in UserService
        return null; // return userService.getUserByEmail(email);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user) {
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
