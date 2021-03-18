package com.revature.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.Business;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that handles {@link User} validity and uses {@link UserRepository}
 * to interact with the database
 */
@Service
public class UserService {

    /**
     * UserRepository that is managed by Spring
     */
    private UserRepository userRepo;

    /**
     * Constructor for UserService
     * @param repo the UserRepository from Spring
     */
    @Autowired
    public UserService(UserRepository repo) {
        super();
        this.userRepo = repo;
    }

    /**
     * Gets a single User using the id
     * @param id the id value of the User
     * @return the User object with the desired id
     */
    public User getUserById(int id) {
        if (id <= 0 ) {
            throw new InvalidRequestException();
        }
        return userRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Adds a User to the database
     * @param newUser the User to add
     */
    public void register(User newUser) {

        if (!isUserValid(newUser)) throw new InvalidRequestException();

        if (userRepo.findUserByUsername(newUser.getUsername()).isPresent()) {
            throw new ResourcePersistenceException("Username is already in use!");
        }

        newUser.setPassword(BCrypt.withDefaults().hashToString(12, newUser.getPassword().toCharArray()));
        newUser.setRegisterDatetime(Timestamp.valueOf(LocalDateTime.now()));
        newUser.setActive(true);
        userRepo.save(newUser);
    }

    /**
     * Gets all User object
     * @return a List of User objects
     */
    public List<User> getAllUsers() {

        List<User> users = (List<User>) userRepo.findAll();

        if (users.isEmpty()) {
            //System.out.println("did we get here?");
            throw new ResourceNotFoundException();
        }

        return users;
    }


    /**
     * Gets the Users with a specified {@link Role}
     * @param role the Role ENUM to search for
     * @return a List of User objects with the desired Role
     */
    public List<User> getUsersByRole(Role role) {

        List<User> users;

        if (role == null) {
            throw new InvalidRequestException();
        }

        users = userRepo.findUsersByRole(role.toString());

        if (users.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return users;

    }

    /**
     * Gets a single User by their username
     * @param username the User's username to find
     * @return a User object with the desired username
     */
    public User getUserByUsername(String username) {

        if (username == null || username.trim().equals("")) {
            throw new InvalidRequestException();
        }

        return userRepo.findUserByUsername(username).orElseThrow(ResourceNotFoundException::new);

    }

    /**
     * Gets a single User by their email
     * @param email the String email of the User
     * @return the User Object with the desired email
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().equals("")) {
            throw new InvalidRequestException();
        }

        return userRepo.findUserByEmail(email).orElseThrow(ResourceNotFoundException::new);
    }


    public boolean confirmAccount(int userId) {

        if (userId <= 0) {
            throw new InvalidRequestException();
        }

        return userRepo.existsById(userId);// confirmAccount(userId);

    }

//    public SortedSet<User> sortUsers(String sortCriterion, Set<User> usersForSorting) {
//
//        SortedSet<User> users = new TreeSet<>(usersForSorting);
//
//        switch (sortCriterion.toLowerCase()) {
//            case "username":
//                users = users.stream()
//                        .collect(Collectors.toCollection(() -> {
//                            return new TreeSet<>(Comparator.comparing(User::getUsername, String::compareTo));
//                        }));
//                break;
//            case "first":
//                users = users.stream()
//                        .collect(Collectors.toCollection(() -> {
//                            return new TreeSet<>(Comparator.comparing(User::getFirstName, String::compareTo));
//                        }));
//                break;
//            case "last":
//                users = users.stream()
//                        .collect(Collectors.toCollection(() -> {
//                            return new TreeSet<>(Comparator.comparing(User::getLastName, String::compareTo));
//                        }));
//                break;
//            case "role":
//                users = users.stream()
//                        .collect(Collectors.toCollection(() -> {
//                            return new TreeSet<>(Comparator.comparing(User::getRole, Enum::compareTo));
//                        }));
//                break;
//            default:
//                throw new ResourceNotFoundException();
//
//        }
//
//        return users;
//
//    }

    /**
     * Provides a User object that corresponds to the given username & password
     * @param username the username of the User
     * @param password the password of the User
     * @return the User object
     */
    public User authenticate(String username, String password) {

        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException();
        }

        User authUser = userRepo.findUserByUsernameAndPassword(username, password).orElseThrow(InvalidRequestException::new);

        if (authUser.isActive()) {
            return authUser;
        } else {
            throw new InvalidRequestException("Account innactive.");
        }

    }

    /**
     * Updates a User in the database
     * @param updatedUser the User object to update
     */
    public void updateProfile(User updatedUser) {
        if (!isUserValid(updatedUser)) {
            throw new InvalidRequestException();
        }

        Optional<User> persistedUser = userRepo.findUserByUsername(updatedUser.getUsername());
        if (persistedUser.isPresent() && persistedUser.get().getUserId() != updatedUser.getUserId()) {
            throw new ResourcePersistenceException("That username is taken by someone else!");
        }

        userRepo.save(updatedUser);

    }

    /**
     * Deletes a User
     * @param user the User object to delete
     */
    public void delete(User user) {
        userRepo.delete(user);
    }

    /**
     * Gets the favorites of a User by User id
     * @param id the id of the User
     * @return a List of Business objects that the User has favorited
     */
    public List<Business> findFavorites(int id) {
        if (id <= 0) {
            throw new InvalidRequestException();
        }

        User user = userRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
        return user.getFavorites();
    }

    /**
     * Checks the User object's fields for validity
     * @param user the User object to check
     * @return a boolean for if the User is valid or not
     */
    public Boolean isUserValid(User user) {
        System.out.println(user);
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        if (user.getPassword() == null || user.getPassword().trim().equals("")) return false;
        return true;
    }

}