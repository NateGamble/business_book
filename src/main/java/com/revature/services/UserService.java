package com.revature.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    //private static final Logger LOG = LogManager.getLogger(UserService.class);
    private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository repo) {
        super();
        this.userRepo = repo;
    }

    //DONE TESTING
    public User getUserById(int id) {
        if (id <= 0 ) {
            throw new InvalidRequestException();
        }
        return userRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    //DONE TESTING
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

    //DONE TESTING
    public List<User> getAllUsers() {

        List<User> users = (List<User>) userRepo.findAll();

        if (users.isEmpty()) {
            //System.out.println("did we get here?");
            throw new ResourceNotFoundException();
        }

        return users;
    }


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

    public User getUserByUsername(String username) {

        if (username == null || username.trim().equals("")) {
            throw new InvalidRequestException();
        }

        return userRepo.findUserByUsername(username).orElseThrow(ResourceNotFoundException::new);

    }

    public boolean confirmAccount(int userId) {

        if (userId <= 0) {
            throw new InvalidRequestException();
        }

        return userRepo.existsById(userId);// confirmAccount(userId);

    }

    public SortedSet<User> sortUsers(String sortCriterion, Set<User> usersForSorting) {

        SortedSet<User> users = new TreeSet<>(usersForSorting);

        switch (sortCriterion.toLowerCase()) {
            case "username":
                users = users.stream()
                        .collect(Collectors.toCollection(() -> {
                            return new TreeSet<>(Comparator.comparing(User::getUsername, String::compareTo));
                        }));
                break;
            case "first":
                users = users.stream()
                        .collect(Collectors.toCollection(() -> {
                            return new TreeSet<>(Comparator.comparing(User::getFirstName, String::compareTo));
                        }));
                break;
            case "last":
                users = users.stream()
                        .collect(Collectors.toCollection(() -> {
                            return new TreeSet<>(Comparator.comparing(User::getLastName, String::compareTo));
                        }));
                break;
            case "role":
                users = users.stream()
                        .collect(Collectors.toCollection(() -> {
                            return new TreeSet<>(Comparator.comparing(User::getRole, Enum::compareTo));
                        }));
                break;
            default:
                throw new ResourceNotFoundException();

        }

        return users;

    }

    public User authenticate(String username, String password) {

        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException();
        }

        User authUser = userRepo.findUserByUsernameAndPassword(username, password).orElseThrow(InvalidRequestException::new);

        if (authUser.isActive()) {
            return authUser;
        } else {
            throw new InvalidRequestException("Account not confirmed.");
        }

    }

    public boolean updateProfile(User updatedUser) {
        boolean updated;

        if (!isUserValid(updatedUser)) {
            throw new InvalidRequestException();
        }

        // Need to make a way to check if a user is being updated or created (put request can do both)
        updated = true;

        Optional<User> persistedUser = userRepo.findUserByUsername(updatedUser.getUsername());
        if (persistedUser.isPresent() && persistedUser.get().getUserId() != updatedUser.getUserId()) {
            throw new ResourcePersistenceException("That username is taken by someone else!");
        }

        userRepo.save(updatedUser);

        return updated;

    }

    public void delete(User user) {
        userRepo.delete(user);
    }

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