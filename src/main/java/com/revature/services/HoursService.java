package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.repos.BusinessHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class HoursService {

    private BusinessHoursRepository repo;

    @Autowired
    public HoursService(BusinessHoursRepository repo) {
        this.repo = repo;
    }

    public Hours findHoursByHoursId (int id) {
        if (id < 1) {
            throw new InvalidRequestException();
        }

        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public List<Hours> findHoursByBusiness (Business bus) {
        return repo.findHoursByBusiness(bus);
    }

    public void createHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.save(hours);
    }

    public void updateHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.save(hours);
    }

    public void deleteHours (Hours hours) {
        if (!isHoursValid(hours))
            throw new InvalidRequestException();

        repo.delete(hours);
    }

    protected boolean isHoursValid(Hours hours) {
        if (hours.getBusiness() == null) return false;
        if (hours.getDay() <= 0) return false;

        return true;
    }

}
