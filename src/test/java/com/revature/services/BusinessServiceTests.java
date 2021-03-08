package com.revature.services;

import com.revature.models.Business;
import com.revature.models.User;
import com.revature.repos.BusinessRepository;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BusinessServiceTests {
    @Mock
    BusinessRepository bizRepo;

    @InjectMocks
    BusinessService bizServices;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void getAllBusinesses() {
        EasyRandom generator = new EasyRandom();
        List<Business> list = new ArrayList<>();
        Business bizOne = generator.nextObject(Business.class);

        list.add(bizOne);

        when(bizRepo.findAll()).thenReturn(list);

        //test
        List<Business> bizList = bizServices.getAllBusinesses();

        assertEquals(1, bizList.size());
        verify(bizRepo, times(1)).findAll();
        List<Business> bizs = bizServices.getAllBusinesses();

        assertFalse(bizs.isEmpty());
    }

}
