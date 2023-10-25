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

    private String colorTheme;
    private String roundOrSquareButton;
    private String fontSize;

    public User(String userName, String email, String firstName, String lastName, String password, String colorTheme, String roundOrSquareButton, String fontSize) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.colorTheme = colorTheme;
        this.roundOrSquareButton = roundOrSquareButton;
        this.fontSize = fontSize;
    }

    @Ignore
    public User(@NonNull String userName, String password) {
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

    public String getColorTheme() {
        return colorTheme;
    }
    public void setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
    }
    public String getRoundOrSquareButton() {
        return roundOrSquareButton;
    }
    public void setRoundOrSquareButton(String roundOrSquareButton) {
        this.roundOrSquareButton = roundOrSquareButton;
    }

    public String getFontSize() {
        return fontSize;
    }
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

}

