package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.*;
import com.revature.repos.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service class that handles data validation for Businesses and uses a BusinessRepository
 * to interact with the database
 */
@Service
public class BusinessService {

    /**
     * BusinessRepository managed by Spring
     */
    private BusinessRepository businessRepo;

    /**
     * Constructor for the BusinessService class
     * @param repo the BusinessRepository
     */
    @Autowired
    public BusinessService(BusinessRepository repo) {
        super();
        this.businessRepo = repo;
    }

    /**
     * Gets a singular Business by the id
     * @param id the id of the Business to find
     * @return the Business object with the given id
     */
    public Business getBusinessById(int id) {
        if (id < 1) {
            throw new InvalidRequestException();
        }

        return businessRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a singular Business by the email
     * @param email the email of the Business to find
     * @return the Business object with the given email
     */
    public Business getBusinessByEmail(String email) {
        if (email == null || email.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByEmail(email).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a singular Business by the name
     * @param businessName the String name of the Business to find
     * @return the Business object with the given name
     */
    public Business getBusinessByBusinessName(String businessName) {
        if (businessName == null || businessName.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByBusinessName(businessName).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a singular Business by location
     * @param location the String address of the Business to find
     * @return the Business object with the given name
     */
    public Business getBusinessByLocation(String location) {
        if (location == null || location.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByLocation(location).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a List of Businesses by the type
     * @param type the type of Businesses to search for
     * @return a List of Business objects with the desired type
     */
    public List<Business> getBusinessesByType(String type) {
        if (type == null || type.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessesByBusinessType(type);
    }

    /**
     * Gets a singular Business by registration time
     * @param time the time that the Business was registered
     * @return the Business object with the desired Timestamp
     */
    public Business getBusinessByRegistrationDate(Timestamp time) {
        if (time == null) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByRegisterDatetime(time).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets all Businesses
     * @return a List of Business objects
     */
    public List<Business> getAllBusinesses() {

        List<Business> businesses;

        businesses = (List<Business>) businessRepo.findAll();

        if (businesses.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return businesses;
    }

    /**
     * Adds a Business to the database
     * @param business the Business object to add to the database
     */
    public void addBusiness(Business business) {

        if (!isBusinessValid(business)) throw new InvalidRequestException();

        if (businessRepo.findBusinessByEmail(business.getEmail()).isPresent()) {
            throw new ResourcePersistenceException("Business is already in use!");
        }

        businessRepo.save(business);
    }

    /**
     * Gets a List of Businesses by the Owner, which is an {@link User} id
     * @param owner
     * @return
     */
    public List<Business> findBusinessesByOwner(User owner) {
        List<Business> ownerBusinesses;
        if (owner == null) {
            throw new InvalidRequestException();
        }

        ownerBusinesses = businessRepo.findBusinessesByOwner(owner);

        if (ownerBusinesses.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ownerBusinesses;
    }

    /**
     * Deletes a Business by the Business id
     * @param id the id of the Business
     */
    public void deleteBusinessById(int id) {
        if (id < 1) {
            throw new InvalidRequestException();
        }

        businessRepo.deleteById(id);
    }

    /**
     * Updates a Business in the database
     * @param business the Business object to update
     */
    public void updateBusiness(Business business) {
        if (!isBusinessValid(business)) {
            throw new InvalidRequestException("Business object is invalid.");
        }

        businessRepo.save(business);
    }

    /**
     * Checks if the fields inside Business are valid
     * @param business the Business to check for validity
     * @return a boolean of if the Business is valid or not
     */
    public Boolean isBusinessValid(Business business) {
        //System.out.println(business);
        if (business == null) return false;
        if (business.getOwner() == null || business.getOwner().getUserId() <= 0) return false;
        if (business.getBusinessName() == null || business.getBusinessName().trim().equals("")) return false;
        if (business.getBusinessType() == null || business.getBusinessType().trim().equals("")) return false;
        if (business.getEmail() == null || business.getEmail().trim().equals("")) return false;
        if (business.getLocation() == null || business.getLocation().trim().equals("")) return false;
        return true;
    }
}
