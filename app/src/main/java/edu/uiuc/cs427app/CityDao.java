package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing database operations related to the City entity.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Dao
public interface CityDao {
    /**
     * Inserts a City object into the database.
     *
     * @param city The City object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(City city);

    /**
     * Retrieves a list of City objects from the database based on specified criteria.
     *
     * @param cityName         The name of the city to search for.
     * @param stateOrRegionName The name of the state or region associated with the city.
     * @param countryName      The name of the country where the city is located.
     * @return A list of City objects matching the specified criteria.
     */
    @Query("SELECT * FROM Cities WHERE cityName = :cityName " +
            "AND stateOrRegionName = :stateOrRegionName AND countryName = :countryName")
    List<City> findCitiesByName(String cityName, String stateOrRegionName, String countryName);
}
