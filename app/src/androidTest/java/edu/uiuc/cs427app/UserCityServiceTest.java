package edu.uiuc.cs427app;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;
/**
 * Tests for verifying if city locations (latitude and longitude) is obtained, throw exceptions if
 * fails, insert into the database if the coordinates are successfully obtained.
 *
 * @author Sherry Li, Yafeng Liu
 * @version 10/22/2023
 */
@RunWith(AndroidJUnit4.class)
public class UserCityServiceTest {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;

    // Predefined latitude and longitude for testing.
    private double latitude = 1.23;
    private double longitude = 4.56;

    // Predefined throw exception messages.
    private String cityNotFoundMessage = "City not found.";
    private String apiFailureMessage = "API failed.";

    @Before
    /**
     * Create the database for testing.
     */
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, UserCityDatabase.class).build();
        userDao = db.userDao();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }

    @After
    /**
     * Close the database after testing.
     */
    public void closeDb() throws IOException {
        db.close();
    }

    /**
     * Define a mock implementation of ICityLocationVerifier to simulate different
     * scenarios (normal operation, city not found, and API failure) based on a given test case when
     * verifying a city's location.
     */
    private class MockCityLocationVerifier implements ICityLocationVerifier {
        private String testCase;
        public MockCityLocationVerifier(String testCase) {
            this.testCase = testCase;
        }

        @Override
        public City VerifyCityLocation(City city) throws Exception {
            switch(testCase) {
                case "normal":
                    City newCity = city;
                    newCity.setLatitude(latitude);
                    newCity.setLongitude(longitude);
                    return newCity;
                case "city_not_found":
                    throw new Exception(cityNotFoundMessage);
                case "api_failure":
                    throw new Exception(apiFailureMessage);
            }
            return null;
        }
    }

    @Rule
    /**
     * Initializes a JUnit rule to specify and verify expected exceptions in test methods.
     */
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    /**
     * Test for adding a city for a user under normal condition, verifying the correct
     * assignment of city details including name, state, country, latitude, and longitude.
     */
    public void addCityForUserCityVerificationNormalTest() throws Exception {
        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("normal");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "123");
        userDao.insert(user);

        String cityName = "cityName";
        String state = "state";
        String country = "country";
        City city = userCityService.addCityForUser(userName, cityName, state, country);
        Assert.assertEquals(city.getCityName(), cityName);
        Assert.assertEquals(city.getStateOrRegionName(), state);
        Assert.assertEquals(city.getCountryName(), country);
        Assert.assertEquals(city.getLatitude(), latitude, 0.001);
        Assert.assertEquals(city.getLongitude(), longitude, 0.001);
    }

    @Test
    /**
     * Checks that an exception with the expected message is thrown when attempting to add a city
     * for a user if the city is not found.
     */
    public void addCityForUserCityVerificationCityNotFoundTest() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(cityNotFoundMessage);

        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("city_not_found");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "123");
        userDao.insert(user);

        userCityService.addCityForUser(userName, "city", "state", "country");
    }

    @Test
    /**
     * Checks that an exception with the expected message is thrown when attempting to add a city
     * for API failed.
     */
    public void addCityForUserCityVerificationAPIFailureTest() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(apiFailureMessage);

        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("api_failure");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "123");
        userDao.insert(user);

        userCityService.addCityForUser(userName, "city", "state", "country");
    }
}
