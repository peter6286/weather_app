package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewWeatherActivity extends AppCompatActivity {
    private double epsilon = 1e-10;

    /*
    A helper function to update a textView. It takes in the text to update, and the id for the
    placeholder string defined in strings.xml.
     */
    private void updateWeatherInformationHelper(TextView textView, String newText, int textViewStringPlaceholder) {
        textView.setText(getString(textViewStringPlaceholder, newText));
    }

    /*
    The onCreate method creates the weather view to display weather information for a selected city.
    Information like temperature, humidity, city name are shown.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent weatherIntent = getIntent();

        boolean isDefaultTheme = weatherIntent.getBooleanExtra("themePreference", false);
        if (isDefaultTheme) {
            setTheme(R.style.Theme_NonDefault);
        } else {
            setTheme(R.style.Theme_Default);
        }

        setContentView(R.layout.activity_view_weather);

        setTitle(getString(R.string.app_name_with_arg, weatherIntent.getStringExtra("userName")));

        TextView cityNameTextView = findViewById(R.id.cityNameWeatherView);
        TextView dateTextView = findViewById(R.id.dateWeatherView);
        TextView timeTextView = findViewById(R.id.timeWeatherView);
        TextView weatherTextView = findViewById(R.id.weatherWeatherView);
        TextView humidityTextView = findViewById(R.id.humidityWeatherView);
        TextView windSpeedTextView = findViewById(R.id.windSpeedWeatherView);
        TextView temperatureTextView = findViewById(R.id.temperatureWeatherView);
        TextView windDirectionTextView = findViewById(R.id.windDirectionWeatherView);

        // Get current system date and time.
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        if (weatherIntent != null) {
            String cityName = weatherIntent.getStringExtra("cityName");

            // Extract weather information out.
            Gson gson = new Gson();
            String jsonWeatherInfo = weatherIntent.getStringExtra("weatherInfo");
            Weather weatherInfo = gson.fromJson(jsonWeatherInfo, Weather.class);

            // Set the values.
            updateWeatherInformationHelper(cityNameTextView, cityName, R.string.city_name_weather_view);
            updateWeatherInformationHelper(dateTextView, formattedDate, R.string.date_weather_view);
            updateWeatherInformationHelper(timeTextView, formattedTime, R.string.time_weather_view);
            updateWeatherInformationHelper(weatherTextView, weatherInfo.getWeatherDescription(), R.string.weather_weather_view);
            updateWeatherInformationHelper(windDirectionTextView, weatherInfo.getWindDirection(), R.string.wind_direction_weather_view);

            double windSpeed = weatherInfo.getWindSpeed();
            if (Math.abs(windSpeed- Double.MAX_VALUE) < epsilon) {
                updateWeatherInformationHelper(windSpeedTextView, "Not Available", R.string.wind_speed_weather_view);
            } else {
                updateWeatherInformationHelper(windSpeedTextView, String.format("%.3f", windSpeed), R.string.wind_speed_weather_view);
            }

            double humidityPercentage = weatherInfo.getHumidity();
            if (Math.abs(humidityPercentage - Double.MAX_VALUE) < epsilon) {
                updateWeatherInformationHelper(humidityTextView, "Not Available", R.string.humidity_weather_view);
            } else {
                updateWeatherInformationHelper(humidityTextView, String.format("%.3f", humidityPercentage), R.string.humidity_weather_view);
            }

            double temperature = weatherInfo.getTemperature();
            if (Math.abs(temperature - Double.MAX_VALUE) < epsilon) {
                updateWeatherInformationHelper(temperatureTextView, "Not Available", R.string.temperature_weather_view);
            } else {
                updateWeatherInformationHelper(temperatureTextView, String.format("%.3f", temperature), R.string.temperature_weather_view);
            }
        }
    }
}