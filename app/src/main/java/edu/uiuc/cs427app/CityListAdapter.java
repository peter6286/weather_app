package edu.uiuc.cs427app;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.List;

//import javax.xml.stream.Location;

/**
 *Represents an adapter that facilitates the display of cities within a list view.
 *
 * This entity class is designed to render a list of cities in a user-friendly format, providing
 * options to interact with each city item, such as adding a city to the list, removing a city from
 * the list or viewing its details. Each city item is associated with specific user interactions enabled by
 * `UserCityService`, ensuring that operations like removal reflect on the underlying data source.
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

    private UIManager uiManager;

    /** CityListAdapter constructor 
        @param    context       current context
        @param    citiesList    cities list
        @param    userCityService    user city service
        @param    signedInUser    signed in user
        @param    uiManager        ui manager
    **/
    public CityListAdapter(Context context, List<City> citiesList, UserCityService userCityService, String signedInUser, UIManager uiManager) {
        super(context, 0, citiesList);
        inflater = LayoutInflater.from(context);
        this.userCityService = userCityService;
        this.signedInUser = signedInUser;
        this.uiManager = uiManager;
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
        uiManager.setTextViewSize(cityNameView, uiManager.getTextSizePreference());
        Button removeButton = view.findViewById(R.id.customRemoveCityButton);
        uiManager.setButtonStyle(removeButton, uiManager.getButtonPreference());
        Button showWeatherButton = view.findViewById(R.id.customCityWeatherButton);
        Button showMapButton = view.findViewById(R.id.customCityMapButton);
        uiManager.setButtonStyle(showWeatherButton, uiManager.getButtonPreference());
        uiManager.setButtonStyle(showMapButton, uiManager.getButtonPreference());

        City city = getItem(position);

        if (city != null) {
            cityNameView.setText(city.getCityName());
            String cityName = city.getCityName();
            Double cityLatitude = city.getLatitude();
            Double cityLongitude = city.getLongitude();

            /*
            Callback function to remove a city from the list. It grabs the cityId from the "city"
             that has been clicked on and issues a call to the DB to remove that city.
             Then it updates the UI to remove that item from the list, and notifes the data set has
             changed.
             */
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove from DB.
                    userCityService.removeCityForUser(signedInUser, city.getCityID());

                    remove(city);

                    notifyDataSetChanged();
                }
            });

            /*
            Callback function to show the weather view for a particular city that is triggered on
            click.
             */
            showWeatherButton.setOnClickListener(new View.OnClickListener() {
                /*
                onClick creates a new Intent based on the ViewWeatherActivity. It makes a call to
                the weather API to get the weather data for the city that has been clicked on and
                passes this  data to the ViewWeatherActivity. Some additional information like the
                signed-in user and their theme preferences are also sent.
                 */
                @Override
                public void onClick(View v) {
                    WeatherParser weatherTask = new WeatherParser();
                    weatherTask.execute(cityLatitude, cityLongitude);
                    /*
                    Callback listener for the weatherTask which retrieves weather information for
                    the city. When data is available, this call back is executed, and information
                    is loaded and sent to the ViewWeatherActivity to display the weather.
                     */
                    weatherTask.setOnWeatherDataListener(new WeatherParser.OnWeatherDataListener() {
                        @Override
                        public void onWeatherDataAvailable(Weather weatherInfo) {
                            if (weatherInfo != null) {
                                // Weather data is available
                                Gson gson = new Gson();
                                String jsonWeatherInfo = gson.toJson(weatherInfo);

                                Intent viewWeatherIntent = new Intent(getContext(), ViewWeatherActivity.class);
                                viewWeatherIntent.putExtra("weatherInfo", jsonWeatherInfo);

                                viewWeatherIntent.putExtra("userName", uiManager.preferences.getString("userName", null));
                                viewWeatherIntent.putExtra("themePreference", uiManager.getThemePreference());
                                viewWeatherIntent.putExtra("cityId", city.getCityID());
                                viewWeatherIntent.putExtra("cityName", city.getCityName());

                                getContext().startActivity(viewWeatherIntent);
                            } else {
                                // Handle case where weather data is not available
                                Toast.makeText(getContext(), "Unable to retrieve weather data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            /*
            Setting the on click listener for the showMapButton.
             */
            showMapButton.setOnClickListener(new View.OnClickListener() {
                /*
                 onClick handler for the showMapButton. It passes the city name, the latitude, and longitude to the ViewMapActivity to
                 display the map of the city identified by the coordinates.
                 */
                @Override
                public void onClick(View view) {
                    // Get inputCityName, inputLat, inputLon and add to intent bundle
                    // This will be passed to ViewMapActivity to display the map
                    Bundle bundle = new Bundle();
                    bundle.putString("inputCityName", cityName);
                    bundle.putDouble("inputLat", cityLatitude);
                    bundle.putDouble("inputLon", cityLongitude);
                    Intent intent = new Intent(getContext(), ViewMapActivity.class);
                    intent.putExtra("bundle", bundle);
                    getContext().startActivity(intent);
                }
            });

        }

        return view;
    }
}
