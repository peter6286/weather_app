package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing database operations related to the User entity.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Dao
public interface UserDao {
    /**
     * Inserts a User object into the database.
     *
     * @param user The User object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(User user);

    /**
     * Retrieves a list of users based on their first name and last name.
     *
     * @param firstName The first name of the users to search for.
     * @param lastName  The last name of the users to search for.
     * @return A list of User objects matching the specified first name and last name criteria.
     */
    @Query("SELECT * FROM Users WHERE firstName = :firstName AND lastName = :lastName")
    List<User> findUsersByName(String firstName, String lastName);
}
