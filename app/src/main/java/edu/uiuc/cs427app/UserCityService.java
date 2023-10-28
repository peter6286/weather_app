package edu.uiuc.cs427app;

import android.util.Log;

import java.util.List;

public class UserCityService {

    private final LinkUserCityDao linkUserCityDao;
    private final CityDao cityDao;  // Assuming there's a CityDao interface for handling City CRUD operations

    public UserCityService(LinkUserCityDao linkUserCityDao, CityDao cityDao) {
        this.linkUserCityDao = linkUserCityDao;
        this.cityDao = cityDao;
    }

    // 1. List cities for a user
    public List<City> getCitiesForUser(String userName) {
        return linkUserCityDao.findCitiesByUserName(userName);
    }

    // 2. Add a city to a user's list
    public City addCityForUser(String userName, String cityName, String stateOrRegion, String countryName) {
        // 1. Check if the city exists in the database
        List<City> matchingCities = cityDao.findCitiesByName(cityName, stateOrRegion, countryName);
        Log.d("DEBUG", "city count" + matchingCities.stream().count());
        City cityToAdd;

        // 2. If the city does not exist, insert it
        if (matchingCities == null || matchingCities.isEmpty()) {
            City newCity = new City();
            newCity.setCityName(cityName);
            newCity.setStateOrRegionName(stateOrRegion);
            newCity.setCountryName(countryName);

            cityDao.insert(newCity);

            // Fetch the added city from the DB
            matchingCities = cityDao.findCitiesByName(cityName, stateOrRegion, countryName);
        }

        // Assume that the first matched city is the one we're interested in
        cityToAdd = matchingCities.get(0);

        // 3. Link the user with the city
        LinkUserCity link = new LinkUserCity();
        link.setUserName(userName);
        link.setCityID(cityToAdd.getCityID());

        // 4. Call linkUserCityDao.insert() to link user with the city
        try {
            linkUserCityDao.insert(link);
        }
        catch (Exception e) {
            // Dup entry.
            Log.d("DEBUG", "error" + e.toString());
            return null;
        }

        // 5. Return the updated list of cities for the user
        return cityToAdd;
    }


    // 3. Remove a city from a user's list
    public List<City> removeCityForUser(String userName, int cityID) {
        try {
            linkUserCityDao.deleteLinkUserCity(userName, cityID);
        } catch (Exception e) {
            throw new RuntimeException("Error in remove the city for the user");
        }
        return getCitiesForUser(userName);  // Return the updated list of cities for that user
    }
}
