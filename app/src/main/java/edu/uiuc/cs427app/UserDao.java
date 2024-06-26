package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing database operations related to the User entity.
 *
 * @author Sherry Li, Yafeng Liu
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
     * Retrieves a list of users based on their user name.
     *
     * @param userName The name of the users to search for.
     * @return A list of User objects matching the specified user name criteria.
     */
    @Query("SELECT * FROM Users WHERE userName = :userName")
    List<User> findUsersByName(String userName);

    /**
     * Checks if a specific username exists in the database.
     *
     * @param userName The name of the user to search for.
     * @return The number of users with the specified username.
     */
    @Query("SELECT COUNT(*) FROM Users WHERE userName = :userName")
    boolean checkUserExistence(String userName);

    /**
     * Save user UI preferences to the database.
     *
     * @param userName The name of the user to search for.
     * @param userName The name of the user to search for.
     * @param userName The name of the user to search for.
     * @param userName The name of the user to search for.
     * @return The number of users with the specified username.
     */
    @Query("UPDATE Users SET isDefaultTheme=:isDefault, isRounded=:isRounded, isLargeText=:isLargeText WHERE userName = :userName")
    Integer saveUserUI(String userName, Boolean isDefault, Boolean isRounded, Boolean isLargeText);
}