package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<City> cities;
    private CityListAdapter customAdapter;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        // Get custom  cities for now.
        // Replace this with db call for logged in user to see if they have any cities.
        cities = GetCities();

        ListView cityListView = findViewById(R.id.listCityView);
        Button addCityButton = findViewById(R.id.addCityButton);
        customAdapter = new CityListAdapter(this, cities);
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
}