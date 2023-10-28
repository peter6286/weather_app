package edu.uiuc.cs427app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.List;

/**
 * Represents a link between users and cities in the database.
 *
 * This entity class is used to establish a many-to-many relationship between User and City entities
 * by maintaining a mapping of usernames and city IDs.
 *
 * @author Anirudh Prasad
 * @version 10/27/2023
 */

/**
 * ArrayAdapter for displaying a list of cities with functionalities to remove a city and show details about it.
 */
public class CityListAdapter extends ArrayAdapter<City> {
    private LayoutInflater inflater;
    private UserCityDatabase db;
    private LinkUserCityDao linkUserCityDao;
    private SharedPreferences sharedPreferences;
    private CityDao cityDao;
    private UserCityService userCityService;
    private String signedInUser;

    /**
     * Constructor for the CityListAdapter.
     *
     * @param context         Context of the app.
     * @param citiesList      List of cities to be displayed.
     * @param userCityService Service for User-City operations.
     * @param signedInUser    The currently signed-in user.
     */
    public CityListAdapter(Context context, List<City> citiesList, UserCityService userCityService, String signedInUser) {
        super(context, 0, citiesList);
        inflater = LayoutInflater.from(context);
        this.userCityService = userCityService;
        this.signedInUser = signedInUser;
    }

    /**
     * Get the view for a city item in the list.
     *
     * @param position    Position of the city in the list.
     * @param convertView Reusable view (if available).
     * @param parent      Parent view to which this item view will be added.
     * @return The view for the city item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_city_list, parent, false);
        }

        TextView cityNameView = view.findViewById(R.id.customCityName);
        Button removeButton = view.findViewById(R.id.customRemoveCityButton);
        Button showDetailsButton = view.findViewById(R.id.customCityShowDetailsButton);

        City city = getItem(position);

        if (city != null) {
            cityNameView.setText(city.getCityName());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove from DB.
                    userCityService.removeCityForUser(signedInUser, city.getCityID());

                    remove(city);

                    notifyDataSetChanged();
                }
            });

            showDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        return view;
    }
}
