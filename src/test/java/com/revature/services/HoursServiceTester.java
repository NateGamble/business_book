package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Business;
import com.revature.models.Hours;
import com.revature.repos.HoursRepository;
import org.jeasy.random.EasyRandom;
import com.revature.repos.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import java.sql.Timestamp;

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

    @BeforeEach
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
    @DisplayName("Verifying findHoursByHoursId() works as expected and pulls hours obj")
    public void testFindHoursByHoursId () {
        when(hoursRepository.findById(hours.getHoursId())).thenReturn(java.util.Optional.of(hours));

        assertEquals(hoursService.findHoursByHoursId(hours.getHoursId()), hours);

        verify(hoursRepository, times(1)).findById(hours.getHoursId());
    }

    @Test
    @DisplayName("Verifying error thrown on findHoursByHoursId() for invalid id or no hours found in repo call")
    public void testFindHoursByHoursIdInvalid () {
        //no findbyid proxy call created for hours repo so should throw error
        assertThrows(ResourceNotFoundException.class, () -> hoursService.findHoursByHoursId(hours.getHoursId()));
        assertThrows(InvalidRequestException.class, () -> hoursService.findHoursByHoursId(0));
    }

    @Test
    @DisplayName("Verifying findHoursByBusiness() works as expected and pulls hours obj")
    public void testFindHoursByBusiness () {
        List<Hours> checkList = new ArrayList<>();

        // Add only Hours that correspond to bus1
        checkList.add(hours);
        checkList.add(hours1);

        when(hoursRepository.findHoursByBusiness(bus1)).thenReturn(checkList);
        assertEquals(hoursService.findHoursByBusiness(bus1), checkList);

        verify(hoursRepository, times(1)).findHoursByBusiness(bus1);
    }

    @Test
    @DisplayName("Verifying createHours() works as expected and pulls hours obj")
    public void testCreateHours() {
        hours.setDay(1);
        hoursService.createHours(hours);
        verify(hoursRepository, times(1)).save(hours);
    }

    @Test
    @DisplayName("Verifying error thrown on createHours() for invalid hours obj")
    public void testCreateHoursInvalid() {
        hours.setDay(0);
        assertThrows(InvalidRequestException.class, () -> hoursService.createHours(hours));
    }

    @Test
    @DisplayName("Verifying updateHours() works as expected and pulls hours obj")
    public void testUpdateHours() {
        hours.setDay(1);
        hoursService.updateHours(hours);
        verify(hoursRepository, times(1)).save(hours);
    }

    @Test
    @DisplayName("Verifying error thrown on updateHours() for invalid hours obj")
    public void testUpdateHoursInvalid() {
        hours.setDay(0);
        assertThrows(InvalidRequestException.class, () -> hoursService.updateHours(hours));
    }

    @Test
    @DisplayName("Verifying deleteHours() works as expected and pulls hours obj")
    public void testDeleteHours() {
        hours.setDay(1);
        hoursService.deleteHours(hours);
        verify(hoursRepository, times(1)).delete(hours);
    }

    @Test
    @DisplayName("Verifying error thrown on deleteHours() for invalid hours obj")
    public void testDeleteHoursInvalid() {
        hours.setDay(0);
        assertThrows(InvalidRequestException.class, () -> hoursService.deleteHours(hours));
    }

    @Test
    @DisplayName("Verifying error thrown on isHoursValid() returns true for valid entry")
    public void testIsHoursValid() {
        hours.setDay(1);
        assertTrue(hoursService.isHoursValid(hours));
    }

    @Test
    @DisplayName("Verifying error thrown on isHoursValid() for null business or day on hours obj")
    public void testIsHoursValidInvalid() {
        hours.setDay(0);
        assertFalse(hoursService.isHoursValid(hours));
        hours.setDay(1);
        assertTrue(hoursService.isHoursValid(hours));
        hours.setBusiness(null);
        assertFalse(hoursService.isHoursValid(hours));
    }
}
