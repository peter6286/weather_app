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

public class CityInputActivity extends AppCompatActivity {

    private UserCityDatabase db;
    private LinkUserCityDao linkUserCityDao;
    private SharedPreferences sharedPreferences;
    private CityDao cityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createDb();
        UserCityService userCityService = new UserCityService(linkUserCityDao, cityDao);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String signedInUser = sharedPreferences.getString("username", null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_input);

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

                finish();
            }
        });
    }


    public void createDb() {
        Context context = this;
        db = Room.databaseBuilder(context, UserCityDatabase.class, "database").allowMainThreadQueries().build();
        cityDao = db.cityDao();
        linkUserCityDao = db.linkUserCityDao();
    }
}