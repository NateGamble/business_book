package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.Hours;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HoursRepository extends CrudRepository<Hours, Integer> {

        List<Hours> findHoursByBusiness (Business bus);
}
