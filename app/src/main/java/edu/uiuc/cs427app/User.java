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

    public User(String userName, String email, String firstName, String lastName, String password, Boolean isDefaultTheme, Boolean isRounded, Boolean isLargeText) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isDefaultTheme = isDefaultTheme;
        this.isRounded = isRounded;
        this.isLargeText = isLargeText;
    }

    @Ignore
    public User(@NonNull String userName, String password ) {
        this.userName = userName;
        this.password = password;
    }

    // get username
    @NonNull
    public String getUserName() {
        return userName;
    }

    // set username
    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    // get email
    public String getEmail() {
        return email;
    }

    //set email
    public void setEmail(String email) {
        this.email = email;
    }

    //get first name
    public String getFirstName() {
        return firstName;
    }

    // set first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //get last name
    public String getLastName() {
        return lastName;
    }

    //set last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //get password
    public String getPassword() {
        return password;
    }

    //set password
    public void setPassword(String password) {
        this.password = password;
    }

    //get default time
    public Boolean getDefaultTheme() {
        return isDefaultTheme;
    }

    //set default time
    public void setDefaultTheme(Boolean isDefault) {
        this.isDefaultTheme = isDefault;
    }

    //get is rounded
    public Boolean getIsRounded() {
        return isRounded;
    }

    //set is rounded
    public void setIsRounded(Boolean isRounded) {
        this.isRounded = isRounded;
    }

    //get is large text
    public Boolean getIsLargeText() {
        return isLargeText;
    }

    // set is large text
    public void setIsLargeText(Boolean isLarge) {
        this.isLargeText = isLarge;
    }

}

