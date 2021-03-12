package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.User;
import com.revature.models.Review;
import com.revature.models.Role;
import com.revature.models.User;
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


//@RunWith(MockitoJUnitRunner.class)
public class UserServiceTester {

    User userOne;
    User userTwo;
    User userThree;
    List<User> list;
    User testEmptyUser;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //Create a random generator for easy declaration
        EasyRandom generator = new EasyRandom();
        list = new ArrayList<User>();
        userOne = generator.nextObject(User.class);
        userTwo = generator.nextObject(User.class);
        userThree = generator.nextObject(User.class);

        list.add(userOne);
        list.add(userTwo);
        list.add(userThree);

        testEmptyUser = new User();
    }

    @Test
    @DisplayName("Verifying getAllUsers() works as expected and pulls all Users")
    public void getAllUsers() {
        when(userRepo.findAll()).thenReturn(list);

        assertEquals(userService.getAllUsers(), list);
        verify(userRepo, times(1)).findAll();
        assertEquals(3, userService.getAllUsers().size());
        verify(userRepo, times(2)).findAll();
    }

    @Test
    @DisplayName("Verifying getAllUsers() throws error when no Users found")
    public void getAllUsersButNoUsers() {
        list.removeAll(list);
        when(userRepo.findAll()).thenReturn(list);

        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
    }

    @Test
    @DisplayName("Verifying getUserById() works as expected and pulls user")
    public void getUserById() {
        //create a 'proxy call' for the User repository 'findById()' method to hone in on the service class
        when(userRepo.findById(userTwo.getUserId())).thenReturn(java.util.Optional.of(userTwo));

        assertEquals(userService.getUserById(userTwo.getUserId()), userTwo);
        verify(userRepo, times(1)).findById(userTwo.getUserId());
    }

    @Test
    @DisplayName("Verifying errors thrown on getUserById() as expected")
    public void getUserByIdCheckInvalidId() {
        //check valid id not in db
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(13));
        verify(userRepo, times(1)).findById(13);
        //check negative id entry
        assertThrows(InvalidRequestException.class, () -> userService.getUserById(-1));
        verify(userRepo, times(0)).findById(-1);
    }

    @Test
    @DisplayName("Verifying getUsersByRole() works as expected and pulls users by role")
    public void getUsersByRole() {
        //create a 'proxy call' for the User repository 'findUsersByRole to hone in on service testing
        when(userRepo.findUsersByRole(userTwo.getRole().toString())).thenReturn(list);

        assertEquals(userService.getUsersByRole(userTwo.getRole()), list);
        verify(userRepo, times(1)).findUsersByRole(userTwo.getRole().toString());
    }

    @Test
    @DisplayName("Verifying errors thrown on getUsersByRoleInvalid for null role or no users found with role")
    public void getUsersByRoleInvalid() {
        //check null role
        assertThrows(InvalidRequestException.class, () -> userService.getUsersByRole(null));
        //check if no users in list with role
        list.removeAll(list);
        when(userRepo.findUsersByRole(userTwo.getRole().toString())).thenReturn(list);
        assertThrows(ResourceNotFoundException.class, () -> userService.getUsersByRole(userTwo.getRole()));
    }

    @Test
    @DisplayName("Verifying getUserByUsername() works as expected and pulls User")
    public void getUserByUsername() {
        //create a 'proxy call' for the User repository 'findById()' method to hone in on the service class
        when(userRepo.findUserByUsername(userTwo.getUsername())).thenReturn(java.util.Optional.of(userTwo));

        assertEquals(userService.getUserByUsername(userTwo.getUsername()), userTwo);
        verify(userRepo, times(1)).findUserByUsername(userTwo.getUsername());
    }

    @Test
    @DisplayName("Verifying error thrown on getUserByName() for null or empty string name")
    public void getUserByNameNotNull() {
        String nullName = null;
        assertThrows(InvalidRequestException.class, () -> userService.getUserByUsername(nullName));
        String emptyName = "";
        assertThrows(InvalidRequestException.class, () -> userService.getUserByUsername(emptyName));

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername(userOne.getUsername()));
    }


    //ALSO AN OWNER FUNCTION*
    //FAILING BECAUSE WE HAVE A GETUSERID CALL IN User TOSTRING METHOD THAT IS WORKING ON AN OWNER
    //IF NO OWNER EXISTS (IE WE HAVE A NULL User BEING SENT IN) IT BLOWS UP IN THE TOSTRING
    //(BECAUSE IT TRYS PRINTING IT OUT IN ISUserVALID IN BIZSERVICE)
    @Test
    @DisplayName("Verifying createNewUser can save User as expected")
    public void createNewUser() {
        when(userRepo.save(userOne)).thenReturn(userOne);
        userService.register(userOne);
        verify(userRepo, times(1)).save(userOne);
    }

    @Test
    @DisplayName("Verifying errors thrown on createNewUser for invalid User")
    public void registerUserCheckInvalid() {
        when(userRepo.save(testEmptyUser)).thenReturn(testEmptyUser);

        assertThrows(InvalidRequestException.class, () -> userService.register(testEmptyUser));

        //double check to make sure we don't actually get to the save method on any of our calls
        verify(userRepo, times(0)).save(testEmptyUser);
    }

    @Test
    @DisplayName("Verifying errors thrown on createNewUser for email in use")
    public void registerUserCheckUsedUsername() {
        when(userRepo.save(userTwo)).thenReturn(userTwo);
        when(userRepo.findUserByUsername(userTwo.getUsername())).thenReturn(java.util.Optional.of(userTwo));

        assertThrows(ResourcePersistenceException.class, () -> userService.register(userTwo));

        //double check to make sure we don't actually get to the save method on any of our calls
        verify(userRepo, times(0)).save(userTwo);
    }

    //TODO: NEED TO IMPLEMENT DELETE METHOD IN User SERVICE (NOT FINISHED)
    //ALSO AN OWNER FUNCTION*
    @Test
    public void deleteUser() {
        userService.delete(userOne);
        verify(userRepo, times(1)).delete(userOne);
    }


    //Internal function
    @Test
    @DisplayName("Verifying isUserValid() is false with null User")
    public void checkUserValidNotNull() {
        //check that null throws error
        testEmptyUser = null;
        assertFalse(userService.isUserValid(testEmptyUser));
    }

    @Test
    @DisplayName("Verifying isUserValid() is false with null or empty string firstname")
    public void checkUserValidFirstName() {
        //set userOne null
        userOne.setFirstName(null);
        assertFalse(userService.isUserValid(userOne));
        userOne.setFirstName("");
        assertFalse(userService.isUserValid(userOne));
    }

    @Test
    @DisplayName("Verifying isUserValid() is false with null or empty string lastname")
    public void checkUserValidLastName() {
        //set userOne null
        userOne.setLastName(null);
        assertFalse(userService.isUserValid(userOne));
        userOne.setLastName("");
        assertFalse(userService.isUserValid(userOne));
    }

    @Test
    @DisplayName("Verifying isUserValid() is false with null or empty string Username")
    public void checkUserValidUsername() {
        userOne.setUsername("");
        assertFalse(userService.isUserValid(userOne));
        userOne.setUsername(null);
        assertFalse(userService.isUserValid(userOne));
    }

    @Test
    @DisplayName("Verifying isUserValid() is false with null or empty string Password")
    public void checkUserValidPassword() {
        userOne.setPassword("");
        assertFalse(userService.isUserValid(userOne));
        userOne.setPassword(null);
        assertFalse(userService.isUserValid(userOne));
    }

    @Test
    @DisplayName("Verifying confirmAccount with invalid ID throws error")
    public void checkConfirmAccountInvalidId() {
        userOne.setUserId(0);
        assertThrows(InvalidRequestException.class, () -> userService.confirmAccount(userOne.getUserId()));
    }

    @Test
    @DisplayName("Verifying confirmAccount works as expected")
    public void checkConfirmAccount() {
        userOne.setUserId(1);
        when(userRepo.existsById(userOne.getUserId())).thenReturn(true);
        assertTrue(userService.confirmAccount(userOne.getUserId()));
    }


    @Test
    @DisplayName("Verifying confirmAccount works as expected")
    public void checkUpdateProfile() {
        assertThrows(InvalidRequestException.class, () -> userService.updateProfile(testEmptyUser));
        when(userRepo.findUserByUsername(userTwo.getUsername())).thenReturn(Optional.ofNullable(userOne));
        assertThrows(ResourcePersistenceException.class, () -> userService.updateProfile(userTwo));
        verify(userRepo, times(0)).save(userTwo);
        userService.updateProfile(userOne);
        verify(userRepo, times(1)).save(userOne);

//        assertTrue(userService.confirmAccount(userOne.getUserId()));
    }



    @Test
    @DisplayName("Verifying SortUsers works as expected")
    public void sortUsers() {
//        SortedSet<User> sortedUsers = new TreeSet();
//        userOne.setUsername("a");
//        userTwo.setUsername("b");
//        sortedUsers.add(userOne);
//        sortedUsers.add(userTwo);
        Set<User> unsortedUsers = new TreeSet<>();
        userOne.setUsername("b");
        userTwo.setUsername("a");
        unsortedUsers.add(userOne);
        unsortedUsers.add(userTwo);
        //assertEquals(userService.sortUsers("username", unsortedUsers).first(), userTwo);

    }

}
