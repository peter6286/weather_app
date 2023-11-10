package edu.uiuc.cs427app;

import android.util.Log;

import java.util.List;

/** UserCityService class that houses the functionality to modify and retrieve
a user's associated cities
**/
public class UserCityService {

    private final LinkUserCityDao linkUserCityDao;
    private final CityDao cityDao;  // Assuming there's a CityDao interface for handling City CRUD operations
    private final ICityLocationVerifier cityLocationVerifier;
    /** This is a constructor for the UserCityService class. It takes two dependencies as input: a LinkUserCityDao and a CityDao. These dependencies will be used to access and update the user-city link and city tables in the database.

     The constructor initializes the linkUserCityDao and cityDao fields to the values passed in as parameters. These fields will be used by the service to perform operations on the user-city link and city tables. **/
    public UserCityService(LinkUserCityDao linkUserCityDao, CityDao cityDao, ICityLocationVerifier cityLocationVerifier) {
        this.linkUserCityDao = linkUserCityDao;
        this.cityDao = cityDao;
        this.cityLocationVerifier = cityLocationVerifier;
    }

    /** List cities for a user 
        @param    userName    a user's unique name
        return    a list of cities associated to user
    **/
    public List<City> getCitiesForUser(String userName) {
        return linkUserCityDao.findCitiesByUserName(userName);
    }

    /**  Add a city to a user's list 
         @param    userName         a user's unique name
         @param    cityName         city name
         @param    stateOrRegion    state or region name
         @param    countryName      a country name
         return    addded City
    **/
    public City addCityForUser(String userName, String cityName, String stateOrRegion, String countryName) throws Exception {
        // 1. Check if the city exists in the database
        List<City> matchingCities = cityDao.findCitiesByName(cityName, stateOrRegion, countryName);
        Log.d("DEBUG", "city count" + matchingCities.stream().count());
        City cityToAdd;

        // 2. If the city does not exist, insert it
        if (matchingCities == null || matchingCities.isEmpty()) {
            verifyThenInsertCity(cityName, stateOrRegion, countryName);

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


    /**  Remove a city from a user's list 
         @param    userName         a user's unique name
         @param    cityID           city unique identifier
         return    removed City        
    **/
    public List<City> removeCityForUser(String userName, int cityID) {
        try {
            linkUserCityDao.deleteLinkUserCity(userName, cityID);
        } catch (Exception e) {
            throw new RuntimeException("Error in remove the city for the user");
        }
        return getCitiesForUser(userName);  // Return the updated list of cities for that user
    }

    /**
     * Verifies the location of a city and then inserts it into the database.
     * This method creates a new city object based on provided city name, state or region,
     * and country name. It then verifies the city's location using the cityLocationVerifier
     * if it is not null.
     * After successful verification, the city is inserted into the database using the cityDao.
     *
     * @param cityName The name of the city to be verified and inserted.
     * @param stateOrRegion The state or region where the city is located.
     * @param countryName The country in which the city is situated.
     * @throws Exception If the city's location cannot be verified (e.g., if the city is not found
     *         or the verification API fails),or if there is an issue with inserting the city into
     *         the database.
     */
    private void verifyThenInsertCity(String cityName, String stateOrRegion, String countryName) throws Exception {
        City newCity = new City();
        newCity.setCityName(cityName);
        newCity.setStateOrRegionName(stateOrRegion);
        newCity.setCountryName(countryName);

        // Verify city location.
        if (cityLocationVerifier != null) {
            newCity = cityLocationVerifier.VerifyCityLocation(newCity);
        }

        cityDao.insert(newCity);
    }
}
