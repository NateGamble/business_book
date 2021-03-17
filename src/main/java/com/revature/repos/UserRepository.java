package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
    List<User> findUsersByRole(String role);
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    Optional<User> findUserByEmail(String email);
}