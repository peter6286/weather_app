package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Represents a link between users and cities in the database.
 *
 * This entity class is used to establish a many-to-many relationship between User and City entities
 * by maintaining a mapping of usernames and city IDs.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */

// Define an entity for the "LinkUsersCities" table.
// This table will have a composite primary key consisting of "userName" and "cityID".
// It also establishes foreign key relationships to the User and City entities.
@Entity(tableName = "LinkUsersCities", primaryKeys = {"userName", "cityID"}, foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userName", childColumns = "userName", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "cityID", childColumns = "cityID", onDelete = ForeignKey.CASCADE)
}, indices = {@Index(value = {"userName"}), @Index(value = {"cityID"})})
public class LinkUserCity {
    @NonNull
    public String userName;

    @NonNull
    public int cityID;

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
