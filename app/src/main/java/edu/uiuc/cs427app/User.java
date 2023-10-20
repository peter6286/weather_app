package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a User in the database.
 *
 * This entity class is used to store user information, including their email, name, password,
 * and theme preference.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Entity(tableName = "Users")
public class User {

    @PrimaryKey
    @NonNull
    public String userName;

    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String themePreference;
}
