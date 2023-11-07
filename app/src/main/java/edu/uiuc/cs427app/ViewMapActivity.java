package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap cityMap;
    private String currentCityName;
    private double currentLat;
    private double currentLon;
    private UiSettings mUiSettings;
    private UIManager uiManager;
    /** onCreate method that will extract inputCityName, inputLat, inputLon from intent's bundle and display the map
     @param savedInstanceState current saved state data
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiManager = new UIManager();
        uiManager.preferences = getSharedPreferences("UserUI", MODE_PRIVATE);
        boolean isDefaultTheme = uiManager.getThemePreference();
        // Set the theme based on the preference
        if (isDefaultTheme) {
            setTheme(R.style.Theme_NonDefault);
        } else {
            setTheme(R.style.Theme_Default);
        }
        setContentView(R.layout.activity_view_map);

        // Set title by signed in userName
        String signedInUser = uiManager.preferences.getString("userName", null);
        setTitle(getString(R.string.app_name_with_arg, signedInUser));
        uiManager.currentLayout =  findViewById(R.id.viewMapRoot);
        TextView eMapCityName = findViewById(R.id.mapCityName);
        TextView eMapLat = findViewById(R.id.mapLatitude);
        TextView eMapLon = findViewById(R.id.mapLongitude);
        uiManager.setTextViewSize(eMapCityName, uiManager.getTextSizePreference());
        uiManager.setTextViewSize(eMapLat, uiManager.getTextSizePreference());
        uiManager.setTextViewSize(eMapLon, uiManager.getTextSizePreference());

        // Retrieve inputCityName, inputLat, inputLon from intent bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        currentCityName = bundle.getString("inputCityName");
        currentLat = bundle.getDouble("inputLat");
        currentLon = bundle.getDouble("inputLon");
        eMapCityName.setText(currentCityName);
        eMapLat.setText("Latitude: " + String.valueOf(currentLat));
        eMapLon.setText("Longitude: " + String.valueOf(currentLon));

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }
    /** onMapReady method that will render the map based on currentLat and currentLon
     @param googleMap current googleMap object
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        cityMap = googleMap;
        mUiSettings = cityMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);

        if (cityMap != null) {
            cityMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Create a LatLng object with the provided latitude and longitude
            LatLng location = new LatLng(currentLat, currentLon);

            // Add a marker on the map
            cityMap.addMarker(new MarkerOptions().position(location).title(currentCityName));

            // Move the camera to the marker location and set an appropriate zoom level
            cityMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

        } else {
            Toast.makeText(this, "Map is not available.", Toast.LENGTH_SHORT).show();
        }
    }
}