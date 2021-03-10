package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.repos.HoursRepository;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HoursServiceTester {

    Hours hours;
    Hours hours1;
    Hours hours2;
    List<Hours> list;
    Business bus1;
    Business bus2;

    @InjectMocks
    HoursService hoursService;

    @Mock
    HoursRepository hoursRepository;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks(this);
        EasyRandom generator = new EasyRandom();
        hours = generator.nextObject(Hours.class);
        hours1 = generator.nextObject(Hours.class);
        hours2 = generator.nextObject(Hours.class);
        list = new ArrayList<>();
        hours.setHoursId(1);
        hours.setHoursId(2);
        hours.setHoursId(3);

        bus1 = generator.nextObject(Business.class);
        bus2 = generator.nextObject(Business.class);
        bus1.setId(1);
        bus2.setId(2);

        hours.setBusiness(bus1);
        hours1.setBusiness(bus1);
        hours2.setBusiness(bus2);

        list.add(hours);
        list.add(hours1);
        list.add(hours2);
    }

    @Test
    public void testFindHoursByHoursId () {
        when(hoursRepository.findById(hours.getHoursId())).thenReturn(java.util.Optional.of(hours));

        Hours testHours = hoursService.findHoursByHoursId(1);

        assertEquals(testHours.getHoursId(), hours.getHoursId());
        verify(hoursService, times(1)).findHoursByHoursId(hours.getHoursId());
        assertEquals(testHours, hours);

        assertThrows(InvalidRequestException.class, () -> hoursService.findHoursByHoursId(0));
    }

    @Test
    public void testFindHoursByBusiness () {
        List<Hours> checkList = new ArrayList<>();

        // Add only Hours that correspond to bus1
        checkList.add(hours);
        checkList.add(hours1);

        when(hoursRepository.findHoursByBusiness(bus1)).thenReturn(checkList);

        List<Hours> testList = hoursService.findHoursByBusiness(bus1);

        assertEquals(testList.size(), 2);
        verify(hoursService, times(1)).findHoursByBusiness(bus1);
        assertEquals(testList, checkList);
    }
}
