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

@Service
public class BusinessService {
    private BusinessRepository businessRepo;

    @Autowired
    public BusinessService(BusinessRepository repo) {
        super();
        this.businessRepo = repo;
    }

    public Business getBusinessById(int id) {
        if (id < 1) {
            throw new InvalidRequestException();
        }

        return businessRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Business getBusinessByEmail(String email) {
        if (email == null || email.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByEmail(email).orElseThrow(ResourceNotFoundException::new);
    }

    public Business getBusinessByBusinessName(String businessName) {
        if (businessName == null || businessName.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByBusinessName(businessName).orElseThrow(ResourceNotFoundException::new);
    }

    public Business getBusinessByLocation(String location) {
        if (location == null || location.trim().equals("") ) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByLocation(location).orElseThrow(ResourceNotFoundException::new);
    }

    public Business getBusinessByRegistrationDate(Timestamp time) {
        if (time == null) {
            throw new InvalidRequestException();
        }

        return businessRepo.findBusinessByRegisterDatetime(time).orElseThrow(ResourceNotFoundException::new);
    }

    public List<Business> getAllBusinesses() {

        List<Business> businesses;

        businesses = (List<Business>) businessRepo.findAll();

        if (businesses.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return businesses;
    }

    public void addBusiness(Business business) {

        if (!isBusinessValid(business)) throw new InvalidRequestException();

        if (businessRepo.findBusinessByEmail(business.getEmail()).isPresent()) {
            throw new ResourcePersistenceException("Business is already in use!");
        }

        businessRepo.save(business);
    }

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
