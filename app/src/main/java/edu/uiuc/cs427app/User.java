package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


/**
 * Represents a User in the database.
 *
 * This entity class is used to store user information, including their email, username,
 * first name, last name, password and theme preference.
 *
 * @author Sherry Li, Sinja Sanandan
 * @version 10/19/2023
 */
@Entity(tableName = "Users")
public class User {

    @PrimaryKey
    @NonNull
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private Boolean isDefaultTheme;
    private Boolean isRounded;
    private Boolean isLargeText;

    public User() {
        this.userName = userName;
        this.password = password;
//        this.isDefaultTheme = isDefaultTheme;
//        this.isRounded = isRounded;
//        this.isLargeText = isLargeText;
    }

    @Ignore
    public User(@NonNull String userName, String password ) {
        this.userName = userName;
        this.password = password;
    }

    // Getter and Setter methods
    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDefaultTheme() {
        return isDefaultTheme;
    }
    public void setDefaultTheme(Boolean isDefault) {
        this.isDefaultTheme = isDefault;
    }
    public Boolean getIsRounded() {
        return isRounded;
    }
    public void setIsRounded(Boolean isRounded) {
        this.isRounded = isRounded;
    }

    public Boolean getIsLargeText() {
        return isLargeText;
    }
    public void setIsLargeText(Boolean isLarge) {
        this.isLargeText = isLarge;
    }

}

