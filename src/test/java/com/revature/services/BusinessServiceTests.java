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



    /**
     * ADMIN FUNCTIONALITY
     */
    //**
    @Test
    public void removeBusinessReview() {

    }

    @Test
    public void getFlaggedReviews() {
    }

    //**
    @Test
    public void getAllBusinesses() {
        EasyRandom generator = new EasyRandom();
        List<Business> list = new ArrayList<>();
        Business bizOne = generator.nextObject(Business.class);
        Business bizTwo = generator.nextObject(Business.class);
        Business bizThree = generator.nextObject(Business.class);
        list.add(bizOne);
        list.add(bizTwo);
        list.add(bizThree);

        when(bizRepo.findAll()).thenReturn(list);

        //test
        List<Business> bizList = bizServices.getAllBusinesses();

        assertEquals(3, bizList.size());
        verify(bizRepo, times(1)).findAll();
        List<Business> bizs = bizServices.getAllBusinesses();

        assertFalse(bizs.isEmpty());
    }


    @Test
    @DisplayName("Verifying getBusinessId() works as expected and pulls business")
    public void getBusinessById() {
        //create a 'proxy call' for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findById(bizTwo.getId())).thenReturn(java.util.Optional.of(bizTwo));

        //test
        Business checkBusiness = bizServices.getBusinessById(bizTwo.getId());

        assertEquals(checkBusiness.getId(), bizTwo.getId());
        verify(bizRepo, times(1)).findById(bizTwo.getId());
        assertEquals(checkBusiness, bizTwo);

        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessById(-1));
    }


    @Test
    @DisplayName("Verifying errors thrown on getBusinessId() as expected")
    public void getBusinessByIdCheckInvalidId() {
        //create a method stub for the business repository 'findById()' method to hone in on the service class
        when(bizRepo.findById(bizTwo.getId())).thenReturn(java.util.Optional.of(bizTwo));
        //check valid id not in db
        assertThrows(ResourceNotFoundException.class, () -> bizServices.getBusinessById(13));
        //check negative id entry
        assertThrows(InvalidRequestException.class, () -> bizServices.getBusinessById(-1));
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
        when(bizRepo.save(bizOne)).thenReturn(bizOne);
        when(bizRepo.save(bizTwo)).thenReturn(bizTwo);
        when(bizRepo.save(bizThree)).thenReturn(bizThree);
        when(bizRepo.findAll()).thenReturn(list);

        //test
        bizServices.addBusiness(bizOne);
        bizServices.addBusiness(bizTwo);
        bizServices.addBusiness(bizThree);

        assertEquals(3, bizServices.getAllBusinesses().size());
        verify(bizRepo, times(1)).save(bizOne);
        verify(bizRepo, times(1)).save(bizTwo);
        verify(bizRepo, times(1)).save(bizThree);
        List<Business> bizs = bizServices.getAllBusinesses();
        assertFalse(bizs.isEmpty());
    }

    /**
     * OWNER FUNCTIONALITY
     */
    @Test
    public void postBusinessUpdate() {

    }

    @Test
    @DisplayName("Verifying findBusinessByOwner works as expected and pulls business")
    public void findBusinessByOwner() {
        List<Business> ownerBusinesses = new ArrayList<>();
        ownerBusinesses.add(bizTwo);
        when(bizRepo.findBusinessesByOwner(owner)).thenReturn(ownerBusinesses);

    }

    @Test
    @DisplayName("Verifying error thrown on findBusinessByOwner for null business owner")
    public void findBusinessByOwnerNotNull() {

    }

    /**
     * USER FUNCTIONALITY
     */

    //@USER LOCATION = 10 mile range from users current position
    //@SET LOCATION = range specified by user from their current position

    @Test
    public void getBusinessesByUserLocation() {
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

    @Test
    public void getBusinessesBySetLocation() {

    }

    @Test
    public void getBusinessesByUserLocationAndType() {
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

    @Test
    public void getBusinessesBySetLocationAndType() {

    }

    /**
     * Test to retrieve list of businesses by default user location and their open/closed (oc) status
     */
    @Test
    public void getBusinessesByUserLocationAndOCStatus() {

    }

    /**
     * Test to retrieve list of businesses by set location and their open/closed (oc) status
     */
    @Test
    public void getBusinessesBySetLocationAndOCStatus() {

    }

    @Test
    public void markBusinessDisliked() {

    }

    @Test
    public void markBusinessFavorite() {

    }

    @Test
    public void markReviewInnapropriate() {

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
