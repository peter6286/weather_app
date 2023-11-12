package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/** Class that allows users to input a city to view.
    Methods included in this class will construct the 
    city input page as well as set up the database.
**/
public class CityInputActivity extends AppCompatActivity {

    private UserCityDatabase db;
    private LinkUserCityDao linkUserCityDao;
    private SharedPreferences sharedPreferences;
    private CityDao cityDao;
    private UIManager uiManager;

    /** on create method to set up the city input page
        @param    savedInstanceState    user saved instance data
    **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        ICityLocationVerifier cityLocationVerifier = new Location();
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, cityLocationVerifier);
        uiManager = new UIManager();
        // Obtain a reference to the SharedPreferences named "UserUI" with private access mode.
// SharedPreferences is a simple key-value storage mechanism to store user-specific data persistently.
        sharedPreferences = getSharedPreferences("UserUI", MODE_PRIVATE);

        // Retrieve the "userName" string from SharedPreferences, which represents the username of the signed-in user.
// If no value is found, 'null' is returned.
        String signedInUser = sharedPreferences.getString("userName", null);

        // Set the title of the current activity, using a formatted string that includes the app name and the signed-in user's name.
        setTitle(getString(R.string.app_name_with_arg, signedInUser));

        // Assign the obtained SharedPreferences instance to the preferences property of the uiManager.
// uiManager is assumed to be an instance of a class that manages the user interface.
        uiManager.preferences = sharedPreferences;

        // Retrieve the theme preference using the uiManager and store it in the isDefaultTheme variable.
// The theme preference is typically a boolean indicating whether the user has chosen the default theme.

        boolean isDefaultTheme = uiManager.getThemePreference();
        // Set the theme based on the preference
        if (isDefaultTheme) {
            setTheme(R.style.Theme_NonDefault);
        } else {
            setTheme(R.style.Theme_Default);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_input);

        uiManager.currentLayout =  findViewById(R.id.cityInputRoot);
        uiManager.changeStyleRecursive(uiManager.currentLayout);

        Button addCityTextBoxButton = findViewById(R.id.buttonAddCityTextBox);
        EditText cityTextBox = findViewById(R.id.addCityTextBox);
        EditText stateOrRegionTextBox = findViewById(R.id.addRegionOrStateTextBox);
        EditText countryTextBox = findViewById(R.id.addCountryTextBox);

        // onClickListener to add the city specified in the textBox by the user
        // into the cityList.
        addCityTextBoxButton.setOnClickListener(view -> {
            String cityName = cityTextBox.getText().toString();
            String stateOrRegionName = stateOrRegionTextBox.getText().toString();
            String country = countryTextBox.getText().toString();

            if (!cityName.isEmpty()) {
                Intent resultIntent = new Intent();

                try {
                    // Call to db.
                    City addedCity = userCityService.addCityForUser(signedInUser, cityName, stateOrRegionName, country);
                    if (addedCity != null) {
                        resultIntent.putExtra("cityName", addedCity.getCityName());
                        resultIntent.putExtra("stateOrRegion", addedCity.getStateOrRegionName());
                        resultIntent.putExtra("country", addedCity.getCountryName());
                        resultIntent.putExtra("cityId", addedCity.getCityID());
                        resultIntent.putExtra("cityLat", addedCity.getLatitude());
                        resultIntent.putExtra("cityLon", addedCity.getLongitude());

                        // set result so parent activity can process it.
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        Toast.makeText(CityInputActivity.this, "The city, state/region and country already exists in the list", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CityInputActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

    /** create database for the city input page **/
    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}
