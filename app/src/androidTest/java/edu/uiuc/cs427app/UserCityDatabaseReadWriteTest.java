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
    public void writeUserAndReadInList() throws Exception {
        User user = new User("abc", "123");
        user.userName = "abc";
        user.password = "123";
        userDao.insert(user);
        List<User> usersByName = userDao.findUsersByName("abc");
        Assert.assertEquals(usersByName.size(), 1);
        Assert.assertEquals(usersByName.get(0).userName, "abc");
        Assert.assertEquals(usersByName.get(0).password, "123");
    }

    @Test
    public void writeCityAndReadInList() throws Exception {
        City city = new City();
        city.cityName = "abc";
        city.stateOrRegionName = "def";
        city.countryName = "ghi";
        cityDao.insert(city);
        List<City> citiesByName = cityDao.findCitiesByName("abc", "def", "ghi");
        Assert.assertEquals(citiesByName.size(), 1);
        Assert.assertEquals(citiesByName.get(0).cityName, "abc");
        Assert.assertEquals(citiesByName.get(0).stateOrRegionName, "def");
        Assert.assertEquals(citiesByName.get(0).countryName, "ghi");
    }

    @Test
    public void updateLinkUserCity() throws Exception {
        User user = new User("user1Name", "123");
        user.userName = "user1Name";
        userDao.insert(user);

        City city1 = new City();
        city1.cityName = "city1";
        city1.stateOrRegionName = "abc";
        city1.countryName = "def";
        cityDao.insert(city1);

        City city2 = new City();
        city2.cityName = "city2";
        city2.stateOrRegionName = "abc";
        city2.countryName = "def";
        cityDao.insert(city2);

        List<User> usersByName = userDao.findUsersByName(user.userName);
        String userName = usersByName.get(0).userName;

        List<City> citiesByName1 = cityDao.findCitiesByName(city1.cityName, city1.stateOrRegionName, city1.countryName);
        List<City> citiesByName2 = cityDao.findCitiesByName(city2.cityName, city2.stateOrRegionName, city2.countryName);
        int cityID1 = citiesByName1.get(0).cityID;
        int cityID2 = citiesByName2.get(0).cityID;

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
        Assert.assertEquals(userCities2.get(0).cityID, cityID2);
        Assert.assertEquals(userCities2.get(0).cityName, city2.cityName);
        Assert.assertEquals(userCities2.get(0).stateOrRegionName, city2.stateOrRegionName);
        Assert.assertEquals(userCities2.get(0).countryName, city2.countryName);
    }

    @Test
    public void writeUserAndCheckExistence() throws Exception {
        User user = new User();
        user.userName = "abc";
        user.password = "123";
        userDao.insert(user);
        boolean userExistence = userDao.checkUserExistence("abc");
        Assert.assertTrue(userExistence);
    }
}
