package edu.uiuc.cs427app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main home page of the app, displaying a list of cities associated with the currently signed-in user.
 *
 * This activity provides functionalities for users to view a list of cities, add new cities, and manage their
 * settings. It offers dynamic theme switching and UI adjustments, based on user preferences. Users can also sign out
 * from this page and navigate back to the sign-in page.
 *
 * @author Anirudh Prasad, Quan Nguyen
 * @version 10/27/2023
 */

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<City> cities;
    private CityListAdapter customAdapter;
    private Button eSignOutButton;
    private UIManager uiManager;

    private UserCityDatabase db;
    private LinkUserCityDao linkUserCityDao;
    private SharedPreferences sharedPreferences;
    private CityDao cityDao;

    // ActivityResultLauncher that updates the list when a new city is added.
    // This processes an event sent from CityInputActivity.
    private ActivityResultLauncher<Intent> cityInputLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String cityName = data.getStringExtra("cityName");
                    Integer cityId = data.getIntExtra("cityId", -1);

                    City newCity = new City();
                    newCity.setCityID(cityId);
                    newCity.setCityName(cityName);

                    cities.add(newCity);
                    customAdapter.notifyDataSetChanged();
                }
            }
    );

    /** get city by city name string **/
    private City GetCityByCityName(String cityName) {
        City city = new City();
        city.setCityName(cityName);
        return city;
    }

    /** home page setup method to setup UI and backend **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        super.onCreate(savedInstanceState);
        uiManager = new UIManager();
        // Get theme selection
        uiManager.preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        boolean isDefaultTheme = uiManager.getThemePreference();
        // Set the theme based on the preference
        if (isDefaultTheme) {
            setTheme(R.style.Theme_NonDefault);
        } else {
            setTheme(R.style.Theme_Default);
        }
        setContentView(R.layout.activity_home_page);
        eSignOutButton = findViewById(R.id.signOutButton);
        uiManager.currentLayout =  findViewById(R.id.homePageRoot);
        //uiManager.changeStyleRecursive(uiManager.currentLayout);

        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao);
        // Get custom  cities for now.
        // Replace this with db call for logged in user to see if they have any cities.
        sharedPreferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        String signedInUser = sharedPreferences.getString("userName", null);
        setTitle(getString(R.string.app_name_with_arg, signedInUser));
        cities = userCityService.getCitiesForUser(signedInUser);

        ListView cityListView = findViewById(R.id.listCityView);
        Button addCityButton = findViewById(R.id.addCityButton);
        customAdapter = new CityListAdapter(this, cities, userCityService, signedInUser, uiManager);
        cityListView.setAdapter(customAdapter);
        uiManager.changeStyleRecursive(uiManager.currentLayout);

        // On click listener for the "Add City" button
        // This triggers the CityInputActivity from which a user can type in a city
        // and add it to the list.
        addCityButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, CityInputActivity.class);
            cityInputLauncher.launch(intent);
        });

        eSignOutButton.setOnClickListener(new View.OnClickListener() {
            /** onclick function for sign out button **/
            @Override
            public void onClick(View v) {
                uiManager.preferences.edit().putBoolean("signedIn", false).apply();
                uiManager.setThemePreference(false);
                uiManager.setButtonPreference(false);
                uiManager.setTextSizePreference(false);
                Toast.makeText(HomePageActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    
    @Override
    public void onClick(View view) {

    }

    /** create database for home page **/
    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}
