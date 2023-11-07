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

public class Location implements ICityLocationVerifier {


    interface LatLong {
        @GET("users/{user}/repos")
        Call<String> lat(@Path("user") String user);
    }
    public static void getC(){
//        final String[] data = {"lo"};
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl("https://api.geoapify.com/v1/geocode/")
//                .build();
//
//        LatLong latlong=retrofit.create(LatLong.class);
//        latlong.lat("search?text=amsterdam&apiKey=3a943c3232cb42a4b734f27d55f2989c").enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                Log.d("hj","LL");
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                data[0] ="fail";
//            }
//        });





    }
    OkHttpClient client = new OkHttpClient();

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
    String[] getLatLong(String city, String country) throws IOException, InterruptedException {
        String url = "https://api.geoapify.com/v1/geocode/search?text=" + city + " " + country + "&apiKey=3a943c3232cb42a4b734f27d55f2989c";
        String[] res = {null, null};
        final CountDownLatch latch = new CountDownLatch(1);

        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
                latch.countDown();
            }

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

        latch.await(); // Wait for the latch to be counted down before returning
        return res;
    }

}
