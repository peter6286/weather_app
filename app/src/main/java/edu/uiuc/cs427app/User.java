package edu.uiuc.cs427app;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;
import java.util.ArrayList;


/**
 * Represents a User in the database.
 *
 * This entity class is used to store user information, including their email, username,
 * first name, last name, password and theme preference.
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

    public User() {}

    public User(String userName, String email, String firstName, String lastName, String password, String themePreference) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.themePreference = themePreference;
    }

    // Getter and Setter methods

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThemePreference() {
        return themePreference;
    }

    public void setThemePreference(String themePreference) {
        this.themePreference = themePreference;
    }

}

