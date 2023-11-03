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

@RunWith(AndroidJUnit4.class)
public class UserCityServiceTest {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;

    private double latitude = 1.23;
    private double longitude = 4.56;

    private String cityNotFoundMessage = "City not found.";
    private String apiFailureMessage = "API failed.";

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, UserCityDatabase.class).build();
        userDao = db.userDao();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

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
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void addCityForUserCityVerificationNormalTest() throws Exception {
        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("normal");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "abc@def.com", "a", "bc", "123", true, true, true);
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
    public void addCityForUserCityVerificationCityNotFoundTest() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(cityNotFoundMessage);

        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("city_not_found");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "abc@def.com", "a", "bc", "123", true, true, true);
        userDao.insert(user);

        userCityService.addCityForUser(userName, "city", "state", "country");
    }

    @Test
    public void addCityForUserCityVerificationAPIFailureTest() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(apiFailureMessage);

        ICityLocationVerifier mockCityLocationVerifier = new MockCityLocationVerifier("api_failure");
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, mockCityLocationVerifier);

        String userName = "userName";
        User user = new User(userName, "abc@def.com", "a", "bc", "123", true, true, true);
        userDao.insert(user);

        userCityService.addCityForUser(userName, "city", "state", "country");
    }
}
