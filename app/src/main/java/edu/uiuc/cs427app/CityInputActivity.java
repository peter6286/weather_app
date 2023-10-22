package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CityInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                resultIntent.putExtra("cityName", cityName);
                resultIntent.putExtra("stateOrRegion", stateOrRegionName);
                resultIntent.putExtra("country", country);

                // set result so parent activity can process it.
                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });
    }
}