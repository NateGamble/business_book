package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.repos.HoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles data validation for {@link Hours} and uses a {@link HoursRepository}
 * to interact with the database
 */
@Service
public class HoursService {

    /**
     * The HoursRepository that is managed by Spring
     */
    private HoursRepository repo;

    /**
     * Constructor for HoursService
     * @param repo the HoursRepository
     */
    @Autowired
    public HoursService(HoursRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets an Hours object by the id
     * @param id the id of the Hours to find
     * @return an Hours Object with the desired id
     */
    public Hours findHoursByHoursId (int id) {
        if (id < 1) {
            throw new InvalidRequestException();
        }

        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets a List of Hours for a Business
     * @param bus the Business of the Hours you are looking for
     * @return a List of Hours Objects from the desired Business
     */
    public List<Hours> findHoursByBusiness (Business bus) {
        return repo.findHoursByBusiness(bus);
    }

    /**
     * Adds an Hours object into the database
     * @param hours the Hours object to add
     */
    public void createHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.save(hours);
    }

    /**
     * Updates an Hours object
     * @param hours the Hours object to update
     */
    public void updateHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.save(hours);
    }

    /**
     * Deletes an Hours object from the database
     * @param hours the Hours object to delete
     */
    public void deleteHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.delete(hours);
    }

    /**
     * Checks the fields of an Hours object for validity
     * @param hours the Hours object to check
     * @return a boolean of if the Hours object is valid or not
     */
    protected boolean isHoursValid(Hours hours) {
        if (hours.getBusiness() == null) return false;
        if (hours.getDay() <= 0) return false;

        return true;
    }

}
