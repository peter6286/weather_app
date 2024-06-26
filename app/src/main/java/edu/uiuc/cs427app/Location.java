package edu.uiuc.cs427app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The Location class implements the ICityLocationVerifier interface, providing a method
 * to verify and update the geographical location of a city using external APIs.
 */
public class Location implements ICityLocationVerifier {

    // Interface to define the Retrofit GET request for obtaining latitude and longitude
    interface LatLong {
        @GET("users/{user}/repos")
        Call<String> lat(@Path("user") String user);
    }
    OkHttpClient client = new OkHttpClient();

    /**
     * Verifies and updates the geographical location of the specified city.
     * This implementation uses a combination of the city's name, state or region name, and country
     * name to retrieve the latitude and longitude coordinates.
     * If the coordinates are successfully retrieved from external server, the city object is
     * updated with these new values of latitude and longitude.
     * In case of any failure, such as the city not being found or an error with the external API,
     * an exception is thrown.
     *
     * @param city The city object whose location needs to be verified. This object should contain
     *             initial data like city name, state or region name, and country name.
     * @return The city object with updated latitude and longitude coordinates if the verification
     *         is successful.
     * @throws Exception If the city is not found or if there is a failure in the external API
     *         used to retrieve the coordinates.
     */
    @Override
    public City VerifyCityLocation(City city) throws Exception {
        City result = city;

        StringJoiner joiner = new StringJoiner(",");
        joiner.add(city.getCityName());
        joiner.add(city.getStateOrRegionName());

        String cityStateName = joiner.toString();

        try {
            String[] latlon = getLatLong(cityStateName, city.getCountryName());
            Log.d("DEBUG", "latlon succeed");
            if (latlon.length != 2) {
                throw new Exception("City not found.");
            }
            Log.d("DEBUG", "LAT" + latlon[0].toString());
            Log.d("DEBUG", "LONG" + latlon[1].toString());
            double lat = Double.parseDouble(latlon[0]);
            double lon = Double.parseDouble(latlon[1]);

            result.setLatitude(lat);
            result.setLongitude(lon);
        } catch (Exception e) {
            throw new Exception("API failed." + e.toString());
        }

        return result;
    }


    // Function to get latitude and longitude for a given city and country using Geoapify API
    // Parameters:
    //   - city: The name of the city
    //   - country: The name of the country
    // Returns:
    //   An array of strings containing latitude at index 0 and longitude at index 1
    // Throws:
    //   - IOException: If there's an issue with network communication
    //   - InterruptedException: If the thread is interrupted while waiting for the latch
    String[] getLatLong(String city, String country) throws IOException, InterruptedException {
        String url = "https://api.geoapify.com/v1/geocode/search?text=" + city + " " + country + "&apiKey=3a943c3232cb42a4b734f27d55f2989c";
        String[] res = {null, null};
        final CountDownLatch latch = new CountDownLatch(1);


        // Use OkHttp client to make an asynchronous HTTP request to the Geoapify API

        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {

            // Callback method called when the request fails
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
                latch.countDown();
            }



            // Callback method called when the request is successful

            @Override


            public

            void

            onResponse(@NonNull okhttp3.Call call, @NonNull Response response)

                    throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    Gson g = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        JSONArray featuresArray = jsonObject.getJSONArray("features");
                        JSONObject firstFeature = featuresArray.getJSONObject(0);
                        JSONObject properties = firstFeature.getJSONObject("properties");
                        String lat = properties.getString("lat");
                        String lon = properties.getString("lon");
                        Log.v("kl8", lat + " " + lon);
                        res[0] = lat;
                        res[1] = lon;
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                latch.countDown();
            }
        });
        
        // Wait for the latch to be counted down before returning the result array

        latch.await(); // Wait for the latch to be counted down before returning
        return res;
    }

}
