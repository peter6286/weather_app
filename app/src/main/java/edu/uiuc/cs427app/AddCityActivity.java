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

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<City> cities;
    private CityListAdapter customAdapter;

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

                    City newCity = new City();
                    newCity.setCityName(cityName);

                    cities.add(newCity);
                    customAdapter.notifyDataSetChanged();
                }
            }
    );

    private City GetCityByCityName(String cityName) {
        City city = new City();
        city.setCityName(cityName);
        return city;
    }

    private List<City> GetCities() {
        List<City> cities = new ArrayList<City>();

        cities.add(GetCityByCityName("Toronto"));
        cities.add(GetCityByCityName("Waterloo"));
        cities.add(GetCityByCityName("Bobcaygeon"));

        return cities;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao);
        // Get custom  cities for now.
        // Replace this with db call for logged in user to see if they have any cities.
//        cities = GetCities();
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String signedInUser = sharedPreferences.getString("username", null);
        cities = userCityService.getCitiesForUser(signedInUser);

        ListView cityListView = findViewById(R.id.listCityView);
        Button addCityButton = findViewById(R.id.addCityButton);
        customAdapter = new CityListAdapter(this, cities, userCityService, signedInUser);
        cityListView.setAdapter(customAdapter);

        // On click listener for the "Add City" button
        // This triggers the CityInputActivity from which a user can type in a city
        // and add it to the list.
        addCityButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddCityActivity.this, CityInputActivity.class);
            cityInputLauncher.launch(intent);
        });
    }

    @Override
    public void onClick(View view) {

    }

    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}