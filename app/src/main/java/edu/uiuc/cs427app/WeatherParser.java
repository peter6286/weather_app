package edu.uiuc.cs427app;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import java.io.IOException;

/**
 * This class is for making an API call to a weather service, parsing its JSON response, and returning its information in the form of a Weather object.
 */
public class WeatherParser extends AsyncTask<Void, Void, Weather> {
    private OnWeatherDataListener listener;

    /**
     * Listener interface for receiving weather data.
     */
    public interface OnWeatherDataListener {
        void onWeatherDataAvailable(Weather weatherInfo);
    }

    /**
     * Sets the listener to receive weather data when available.
     * 
     * @param listener The listener to be notified.
     */
    public void setOnWeatherDataListener(OnWeatherDataListener listener) {
        this.listener = listener;
    }

    /**
     * Performs the background task to fetch weather data from an API.
     * 
     * @param voids 
     * @return Weather object containing weather data to display in app.
     */
    @Override
    protected Weather doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        // String latitude = params[0];
        // String longitude = params[1];

        // TODO: change hard-coded values
        double latitude = 37.77493;
        double longitude = -122.41942;
        String apiKey = "526ba0336235568bb65e9f08e2b9d242";
        // String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=526ba0336235568bb65e9f08e2b9d242";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=imperial";

       
        Request request = new Request.Builder()
            .url(apiUrl)
            .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String jsonWeatherResponse = response.body().string();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonWeatherResponse, JsonObject.class);

                JsonObject main = jsonObject.getAsJsonObject("main");
                double temperature = main.get("temp").getAsDouble();
                double humidity = main.get("humidity").getAsDouble();

                JsonObject wind = jsonObject.getAsJsonObject("wind");
                double windSpeed = wind.get("speed").getAsDouble();
                double windDir = wind.get("deg").getAsDouble();
                String windDirection = degreesToCardinal(windDir);

                JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
                JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
                String weatherDescription = weatherObject.get("description").getAsString();

                Weather weatherInfo = new Weather(temperature, weatherDescription, humidity, windSpeed, windDirection);

                return weatherInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Receives the weather data after it has been fetched and notifies the listener.
     * 
     * @param weatherInfo Weather information to be passed to the listener.
     */
    @Override
    protected void onPostExecute(Weather weatherInfo) {
        if (listener != null) {
            listener.onWeatherDataAvailable(weatherInfo);
        }
    }

    /**
     * Helper function to calculate cardinal direction of wind based on retrieved direction (in degrees) from API call
     * @param windDir Direction wind is going, in degrees
     * @return Cardinal direction of wind
     */
    private String degreesToCardinal(double windDir) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(windDir / 45) % 8;
        return directions[index];
    }

}
