package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing database operations related to the LinkUserCity entity.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Dao
public interface LinkUserCityDao {
    /**
     * Inserts a LinkUserCity object into the database.
     *
     * @param linkUserCity The LinkUserCity object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(LinkUserCity linkUserCity);

    /**
     * Retrieves a list of cities associated with a specific user.
     *
     * @param userID The ID of the user whose associated cities are to be retrieved.
     * @return A list of City objects associated with the specified user.
     */
    @Query("SELECT c.* FROM Cities AS c JOIN LinkUsersCities AS uc ON c.cityID = uc.cityID " +
            "WHERE uc.userID = :userID")
    List<City> findCitiesByUserId(int userID);

    /**
     * Deletes a link between a user and a city from the database.
     *
     * @param userID The ID of the user to unlink from the city.
     * @param cityID The ID of the city to unlink from the user.
     */
    @Query("DELETE FROM LinkUsersCities WHERE userID = :userID AND cityID = :cityID")
    void deleteLinkUserCity(int userID, int cityID);
}
