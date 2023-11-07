package edu.uiuc.cs427app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringJoiner;

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
            if (latlon.length != 2) {
                throw new Exception("City not found.");
            }

            double lat = Double.parseDouble(latlon[0]);
            double lon = Double.parseDouble(latlon[1]);

            result.setLatitude(lat);
            result.setLongitude(lon);
        } catch (Exception e) {
            throw new Exception("API failed.");
        }

        return result;
    }
    String[] getLatLong(String city, String country) throws IOException {
        String url="https://api.geoapify.com/v1/geocode/search?text="+city+" "+country+"&apiKey=3a943c3232cb42a4b734f27d55f2989c";
        String[] res = {null,null};
        Log.v("kl","dg3");
        Request request = new Request.Builder()
                .url(url)
                .build();

       client.newCall(request).enqueue(new Callback() {

           @Override
           public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
               Log.v("kl","dg2");
               e.printStackTrace();
           }

           @Override
           public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
               if(response.isSuccessful())
               {
                   Log.v("kl","dg");
                   String myResponse=response.body().string();
                   Gson g = new Gson();
                   try {
                       Log.v("kl7","kn");
                       JSONObject jsonObject = new JSONObject(myResponse);
                       JSONArray featuresArray = jsonObject.getJSONArray("features");
                       JSONObject firstFeature = featuresArray.getJSONObject(0);
                       JSONObject properties = firstFeature.getJSONObject("properties");
                       String lat=properties.getString("lat");
                       String lon=properties.getString("lon");
                       Log.v("kl8",lat+" "+lon);
                       res[0]=lat;
                       res[1]=lon;
//                       String geometry = firstFeature.getString("lat");
//
//
//                       Log.v("kl6",geometry.toString());
                   } catch (JSONException e) {
                       throw new RuntimeException(e);
                   }
//                   LatLongLocation s = g.fromJson(myResponse, LatLongLocation.class);
//                   Log.v("kl5",s.getType());
//                   Log.v("kl4",s.getFeatures().toString());

               }
               else{
                   Log.v("kl","sf");
               }
           }

       });
       return res;
    }

}
