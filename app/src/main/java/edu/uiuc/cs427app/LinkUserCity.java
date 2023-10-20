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
 * by maintaining a mapping of user IDs and city IDs.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Entity(tableName = "LinkUsersCities", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "cityID", childColumns = "cityID", onDelete = ForeignKey.CASCADE)
}, indices = {@Index(value = {"userID"})})
public class LinkUserCity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public int userID;
    public int cityID;
}
