package com.revature.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.revature.models.User;
import com.revature.repos.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {
    //injectmocks actually creates the mock and also injects the dependent mocks that are marked with @mock (userrepo here)
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllUsersTest()
    {
        EasyRandom generator = new EasyRandom();
        List<User> list = new ArrayList<User>();
        User empOne = generator.nextObject(User.class);
        User empTwo = generator.nextObject(User.class);
        User empThree = generator.nextObject(User.class);

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        when(userRepository.findAll()).thenReturn(list);

        //test
        List<User> empList = userService.getAllUsers();

        assertEquals(3, empList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getEmployeeByIdTest()
    {
        EasyRandom generator = new EasyRandom();
        User eric = generator.nextObject(User.class);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(eric));

        User emp = userService.getUserById(1);

        assertEquals(eric.getFirstName(), emp.getFirstName());
        assertEquals(eric.getLastName(), emp.getLastName());
        assertEquals(eric.getEmail(), emp.getEmail());
    }

    @Test
    public void createEmployeeTest()
    {
        EasyRandom generator = new EasyRandom();
        User eric = generator.nextObject(User.class);

        userService.register(eric);

        verify(userRepository, times(1)).save(eric);
    }
}

