package edu.uiuc.cs427app;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Tests for User, City, and UserDao, CityDao, and LinkUserCityDao.
 *
 * @author Sherry Li, Yafeng Liu
 * @version 10/22/2023
 */
@RunWith(AndroidJUnit4.class)
public class UserCityDatabaseReadWriteTest {
    private UserDao userDao;
    private CityDao cityDao;
    private LinkUserCityDao linkUserCityDao;
    private UserCityDatabase db;

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

    @Test
    /**
     * Test for inserting a new user and reading the user from database.
     */
    public void writeUserAndReadInList() throws Exception {
        User user = new User("abc", "abc@def.com", "a", "bc", "123", "preference");
        userDao.insert(user);
        List<User> usersByName = userDao.findUsersByName("abc");
        Assert.assertEquals(usersByName.size(), 1);
        Assert.assertEquals(usersByName.get(0).getUserName(), "abc");
        Assert.assertEquals(usersByName.get(0).getPassword(), "123");
    }

    @Test
    /**
     * Test for inserting a new city and reading the city from database.
     */
    public void writeCityAndReadInList() throws Exception {
        City city = new City();
        city.setCityName("abc");
        city.setStateOrRegionName("def");
        city.setCountryName("ghi");
        cityDao.insert(city);
        List<City> citiesByName = cityDao.findCitiesByName("abc", "def", "ghi");
        Assert.assertEquals(citiesByName.size(), 1);
        Assert.assertEquals(citiesByName.get(0).getCityName(), "abc");
        Assert.assertEquals(citiesByName.get(0).getStateOrRegionName(), "def");
        Assert.assertEquals(citiesByName.get(0).getCountryName(), "ghi");
    }

    @Test
    /**
     * Test for adding and removing cities associated with a user in the database.
     */
    public void updateLinkUserCity() throws Exception {
        User user = new User("user1Name", "abc@def.com", "a", "bc", "123", "preference");
        userDao.insert(user);

        City city1 = new City();
        city1.setCityName("city1");
        city1.setStateOrRegionName("abc");
        city1.setCountryName("def");
        cityDao.insert(city1);

        City city2 = new City();
        city2.setCityName("city2");
        city2.setStateOrRegionName("abc");
        city2.setCountryName("def");
        cityDao.insert(city2);

        List<User> usersByName = userDao.findUsersByName(user.getUserName());
        String userName = usersByName.get(0).getUserName();

        List<City> citiesByName1 = cityDao.findCitiesByName(city1.getCityName(), city1.getStateOrRegionName(), city1.getCountryName());
        List<City> citiesByName2 = cityDao.findCitiesByName(city2.getCityName(), city2.getStateOrRegionName(), city2.getCountryName());
        int cityID1 = citiesByName1.get(0).getCityID();
        int cityID2 = citiesByName2.get(0).getCityID();

        LinkUserCity link1 = new LinkUserCity();
        link1.userName = userName;
        link1.cityID = cityID1;
        linkUserCityDao.insert(link1);

        LinkUserCity link2 = new LinkUserCity();
        link2.userName = userName;
        link2.cityID = cityID2;
        linkUserCityDao.insert(link2);

        List<City> userCities1 = linkUserCityDao.findCitiesByUserName(userName);
        Assert.assertEquals(userCities1.size(), 2);

        linkUserCityDao.deleteLinkUserCity(userName, cityID1);

        List<City> userCities2 = linkUserCityDao.findCitiesByUserName(userName);
        Assert.assertEquals(userCities2.size(), 1);
        Assert.assertEquals(userCities2.get(0).getCityID(), cityID2);
        Assert.assertEquals(userCities2.get(0).getCityName(), city2.getCityName());
        Assert.assertEquals(userCities2.get(0).getStateOrRegionName(), city2.getStateOrRegionName());
        Assert.assertEquals(userCities2.get(0).getCountryName(), city2.getCountryName());
    }

    @Test
    /**
     * Test for checking if a user existed in the database.
     */
    public void writeUserAndCheckExistence() throws Exception {
        User user = new User("abc", "abc@def.com", "a", "bc", "123", "preference");
        userDao.insert(user);
        boolean userExistence = userDao.checkUserExistence("abc");
        Assert.assertTrue(userExistence);
    }

    @Test
    /**
     * Test for equals() method in City class.
     */
    public void testEquals() {
        // Test the same instance
        City city1 = new City();
        city1.setCityName("New York");
        city1.setStateOrRegionName("NY");
        city1.setCountryName("USA");
        city1.setLatitude(40.7128);
        city1.setLongitude(-74.0060);
        Assert.assertTrue(city1.equals(city1));

        // Test another city with the same attributes
        City city2 = new City();
        city2.setCityName("New York");
        city2.setStateOrRegionName("NY");
        city2.setCountryName("USA");
        city2.setLatitude(40.7128);
        city2.setLongitude(-74.0060);
        Assert.assertTrue(city1.equals(city2));

        // Test city with different name
        City city3 = new City();
        city3.setCityName("Los Angeles");
        city3.setStateOrRegionName("CA");
        city3.setCountryName("USA");
        city3.setLatitude(34.0522);
        city3.setLongitude(-118.2437);
        Assert.assertFalse(city1.equals(city3));

        // Test city with small difference in latitude
        City city4 = new City();
        city4.setCityName("New York");
        city4.setStateOrRegionName("NY");
        city4.setCountryName("USA");
        city4.setLatitude(40.7128 + 1e-10);  // Difference smaller than EPSILON
        city4.setLongitude(-74.0060);
        Assert.assertTrue(city1.equals(city4));

        // Test city with significant difference in latitude
        City city5 = new City();
        city5.setCityName("New York");
        city5.setStateOrRegionName("NY");
        city5.setCountryName("USA");
        city5.setLatitude(40.7128 + 1e-7);  // Difference bigger than EPSILON
        city5.setLongitude(-74.0060);
        Assert.assertFalse(city1.equals(city5));

        // Test against null
        Assert.assertFalse(city1.equals(null));

        // Test against other type of object
        Assert.assertFalse(city1.equals("New York"));
    }
}
