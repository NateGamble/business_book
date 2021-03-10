package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.Business;
import com.revature.models.Review;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.BusinessRepository;
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

import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;


//@RunWith(MockitoJUnitRunner.class)
public class BusinessServiceTests {

    Business bizOne;
    Business bizTwo;
    Business bizThree;
    List<Business> list;
    Business testEmptyBiz;
    User owner;
    User badOwner;

    @Mock
    BusinessRepository bizRepo;

    @InjectMocks
    BusinessService bizServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //Create a random generator for easy declaration
        EasyRandom generator = new EasyRandom();
        list = new ArrayList<>();
        bizOne = generator.nextObject(Business.class);
        bizTwo = generator.nextObject(Business.class);
        bizThree = generator.nextObject(Business.class);
        //making sure generated businesses have acceptable business id and owner id values
        owner = generator.nextObject(User.class);
        owner.setUserId(1);
        owner.setRole(Role.OWNER);
        badOwner = null;
        bizOne.setId(1);
        bizTwo.setId(2);
        bizThree.setId(3);
        bizOne.setOwner(owner);
        bizTwo.setOwner(owner);
        bizThree.setOwner(owner);

        testEmptyBiz = new Business();

        list.add(bizOne);
        list.add(bizTwo);
        list.add(bizThree);
    }


    @Test
    @DisplayName("Verifying getAllBusinesses() works as expected and pulls all businesses")
    public void getAllBusinesses() {
        when(bizRepo.findAll()).thenReturn(list);

        assertEquals(bizServices.getAllBusinesses(), list);
        verify(bizRepo, times(1)).findAll();
        assertEquals(3, bizServices.getAllBusinesses().size());
        verify(bizRepo, times(2)).findAll();
    }

    @Test
    @DisplayName("Verifying getAllBusinesses() throws error when no businesses found")
    public void getAllBusinessesButNoBusinesses() {
        list.removeAll(list);
        when(bizRepo.findAll()).thenReturn(list);

        assertThrows(ResourceNotFoundException.class, () -> bizServices.getAllBusinesses());
    }

    @Test
    @DisplayName("Verifying getBusinessById() works as expected and pulls business")
    public void getBusinessById() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findById(bizTwo.getId())).thenReturn(java.util.Optional.of(bizTwo));

        assertEquals(bizServices.getBusinessById(bizTwo.getId()), bizTwo);
        verify(bizRepo, times(1)).findById(bizTwo.getId());
        assertEquals(bizServices.getBusinessById(bizTwo.getId()), bizTwo);
    }

    @Test
    @DisplayName("Verifying errors thrown on getBusinessById() as expected")
    public void getBusinessByIdCheckInvalidId() {
        //check valid id not in db
        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessById(13));
        verify(bizRepo, times(1)).findById(13);
        //check negative id entry
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessById(-1));
        verify(bizRepo, times(0)).findById(-1);
    }

    @Test
    @DisplayName("Verifying getBusinessByRegistrationDate() works as expected and pulls business")
    public void getBusinessByRegistrationDate() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findBusinessByRegisterDatetime(bizTwo.getRegisterDatetime())).thenReturn(java.util.Optional.of(bizTwo));

        assertEquals(bizServices.getBusinessByRegistrationDate(bizTwo.getRegisterDatetime()), bizTwo);
        verify(bizRepo, times(1)).findBusinessByRegisterDatetime(bizTwo.getRegisterDatetime());
    }

    @Test
    @DisplayName("Verifying errors thrown on getBusinessByRegistrationDate() when business registration time is null")
    public void getBusinessByRegistrationDateNotNull() {
        Timestamp sometime = null;
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByRegistrationDate(sometime));

        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessByRegistrationDate(bizOne.getRegisterDatetime()));
    }

    @Test
    @DisplayName("Verifying getBusinessLocation() works as expected and pulls business")
    public void getBusinessByLocation() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findBusinessByLocation(bizTwo.getLocation())).thenReturn(java.util.Optional.of(bizTwo));

        assertEquals(bizServices.getBusinessByLocation(bizTwo.getLocation()), bizTwo);
        verify(bizRepo, times(1)).findBusinessByLocation(bizTwo.getLocation());
    }

    @Test
    @DisplayName("Verifying error thrown on getBusinessLocation() for null or empty string location")
    public void getBusinessByLocationNotNull() {
        String nullLocation = null;
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByLocation(nullLocation));
        String emptyLocation = "";
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByLocation(emptyLocation));

        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessByLocation(bizOne.getLocation()));
    }

    @Test
    @DisplayName("Verifying getBusinessByBusinessName() works as expected and pulls business")
    public void getBusinessByName() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findBusinessByBusinessName(bizTwo.getBusinessName())).thenReturn(java.util.Optional.of(bizTwo));

        assertEquals(bizServices.getBusinessByBusinessName(bizTwo.getBusinessName()), bizTwo);
        verify(bizRepo, times(1)).findBusinessByBusinessName(bizTwo.getBusinessName());
    }

    @Test
    @DisplayName("Verifying error thrown on getBusinessByName() for null or empty string name")
    public void getBusinessByNameNotNull() {
        String nullName = null;
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByBusinessName(nullName));
        String emptyName = "";
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByBusinessName(emptyName));

        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessByBusinessName(bizOne.getBusinessName()));
    }

    @Test
    @DisplayName("Verifying getBusinessByEmail works as expected and pulls business")
    public void getBusinessByEmail() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findBusinessByEmail(bizTwo.getEmail())).thenReturn(java.util.Optional.of(bizTwo));

        assertEquals(bizServices.getBusinessByEmail(bizTwo.getEmail()), bizTwo);
        verify(bizRepo, times(1)).findBusinessByEmail(bizTwo.getEmail());
    }

    @Test
    @DisplayName("Verifying error thrown on getBusinessByEmail for null or empty string email")
    public void getBusinessByEmailNotNull() {
        String nullEmail = null;
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByEmail(nullEmail));
        String emptyEmail = "";
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessByEmail(emptyEmail));

        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessByEmail(bizOne.getEmail()));
    }

    //ALSO AN OWNER FUNCTION*
    //FAILING BECAUSE WE HAVE A GETUSERID CALL IN BUSINESS TOSTRING METHOD THAT IS WORKING ON AN OWNER
    //IF NO OWNER EXISTS (IE WE HAVE A NULL BUSINESS BEING SENT IN) IT BLOWS UP IN THE TOSTRING
    //(BECAUSE IT TRYS PRINTING IT OUT IN ISBUSINESSVALID IN BIZSERVICE)
    @Test
    @DisplayName("Verifying createNewBusiness can save business as expected")
    public void createNewBusiness() {
        when(bizRepo.save(bizOne)).thenReturn(bizOne);
        bizServices.addBusiness(bizOne);
        verify(bizRepo, times(1)).save(bizOne);
    }

    @Test
    @DisplayName("Verifying errors thrown on createNewBusiness for invalid business")
    public void createNewBusinessCheckInvalid() {
        when(bizRepo.save(testEmptyBiz)).thenReturn(testEmptyBiz);

        assertThrows(InvalidRequestException.class, () -> bizServices.addBusiness(testEmptyBiz));

        //double check to make sure we don't actually get to the save method on any of our calls
        verify(bizRepo, times(0)).save(testEmptyBiz);
    }

    @Test
    @DisplayName("Verifying errors thrown on createNewBusiness for email in use")
    public void createNewBusinessCheckUsedEmail() {
        when(bizRepo.save(bizTwo)).thenReturn(bizTwo);
        when(bizRepo.findBusinessByEmail(bizTwo.getEmail())).thenReturn(java.util.Optional.of(bizTwo));

        assertThrows(ResourcePersistenceException.class, () -> bizServices.addBusiness(bizTwo));

        //double check to make sure we don't actually get to the save method on any of our calls
        verify(bizRepo, times(0)).save(bizTwo);
    }

    //TODO: NEED TO IMPLEMENT DELETE METHOD IN BUSINESS SERVICE (NOT FINISHED)
    //ALSO AN OWNER FUNCTION*
    @Test
    public void deleteBusiness() {
    }

    @Test
    @DisplayName("Verifying findBusinessByOwner works as expected and pulls business")
    public void findBusinessByOwner() {
        List<Business> ownerBusinesses = new ArrayList<>();
        ownerBusinesses.add(bizTwo);
        when(bizRepo.findBusinessesByOwner(owner)).thenReturn(ownerBusinesses);

        assertEquals(bizServices.findBusinessesByOwner(owner), ownerBusinesses);
        verify(bizRepo, times(1)).findBusinessesByOwner(owner);
    }

    @Test
    @DisplayName("Verifying error thrown on findBusinessByOwner for null business owner")
    public void findBusinessByOwnerNotNull() {
        assertThrows(InvalidRequestException.class, () -> bizServices.findBusinessesByOwner(badOwner));
    }

    @Test
    @DisplayName("Verifying error thrown on findBusinessByOwner for no owner business found")
    public void findBusinessByOwnerNoBusinesses() {
        List<Business> ownerBusinesses = new ArrayList<>();

        when(bizRepo.findBusinessesByOwner(owner)).thenReturn(ownerBusinesses);

        assertThrows(ResourceNotFoundException.class, () -> bizServices.findBusinessesByOwner(owner));
    }

    //Internal function
    @Test
    @DisplayName("Verifying isBusinessValid() is false with null business")
    public void checkBusinessValidNotNull() {
        //check that null throws error
        testEmptyBiz = null;
        assertFalse(bizServices.isBusinessValid(testEmptyBiz));
    }

    @Test
    @DisplayName("Verifying isBusinessValid() is false with null business owner or owner id less than 1")
    public void checkBusinessValidOwner() {
        //set owner null
        bizOne.setOwner(badOwner);
        assertFalse(bizServices.isBusinessValid(bizOne));
        bizOne.setOwner(owner);
        owner.setUserId(0);
        assertFalse(bizServices.isBusinessValid(bizOne));
    }

    @Test
    @DisplayName("Verifying isBusinessValid() is false with null or empty string business name")
    public void checkBusinessValidBusinessName() {
        bizOne.setBusinessName("");
        assertFalse(bizServices.isBusinessValid(bizOne));
        bizOne.setBusinessName(null);
        assertFalse(bizServices.isBusinessValid(bizOne));
    }

    @Test
    @DisplayName("Verifying isBusinessValid() is false with null or empty string business type")
    public void checkBusinessValidBusinessType() {
        bizOne.setBusinessType(null);
        assertFalse(bizServices.isBusinessValid(bizOne));
        bizOne.setBusinessType("");
        assertFalse(bizServices.isBusinessValid(bizOne));
    }

    @Test
    @DisplayName("Verifying isBusinessValid() is false with null or empty string business email")
    public void checkBusinessValidEmail() {
        bizOne.setEmail(null);
        assertFalse(bizServices.isBusinessValid(bizOne));
        bizOne.setEmail("");
        assertFalse(bizServices.isBusinessValid(bizOne));
    }

    @Test
    @DisplayName("Verifying isBusinessValid() is false with null or empty string business location")
    public void checkBusinessValidLocation() {
        bizOne.setLocation(null);
        assertFalse(bizServices.isBusinessValid(bizOne));
        bizOne.setLocation("");
        assertFalse(bizServices.isBusinessValid(bizOne));
    }


}
