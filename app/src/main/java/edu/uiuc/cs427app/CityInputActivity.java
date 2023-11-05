package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Retrofit;

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
        // TODO: Instantiate cityLocationVerifier.
        ICityLocationVerifier cityLocationVerifier = null;
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao, cityLocationVerifier);
        uiManager = new UIManager();
        sharedPreferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        String signedInUser = sharedPreferences.getString("userName", null);
        setTitle(getString(R.string.app_name_with_arg, signedInUser));
        uiManager.preferences = sharedPreferences;
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
