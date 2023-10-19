package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int UserID;

    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String themePreference;
}
