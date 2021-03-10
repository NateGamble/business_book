package com.revature.repos;

import com.revature.models.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends CrudRepository<Business, Integer> {
    List<Business> findBusinessesByOwner(User owner);
    List<Business> findBusinessesByBusinessType(String type);
    Optional<Business> findBusinessByEmail(String email);
    Optional<Business> findBusinessByBusinessName(String businessName);
    Optional<Business> findBusinessByLocation(String location);
    Optional<Business> findBusinessByRegisterDatetime(Timestamp registerDatetime);
}