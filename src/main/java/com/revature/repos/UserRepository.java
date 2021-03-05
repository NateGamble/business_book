package com.revature.repos;

import com.revature.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
    Set<User> findUsersByRole(String role);
    Optional<User> findUserByUsernameAndPassword(String username, String password);

//    @Query(value = "update User set accountConfirmed = true where id = :id")
//    void confirmAccount(int id);

}