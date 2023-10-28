package edu.uiuc.cs427app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.List;

public class CityListAdapter extends ArrayAdapter<City> {
    private LayoutInflater inflater;
    private UserCityDatabase db;
    private LinkUserCityDao linkUserCityDao;
    private SharedPreferences sharedPreferences;
    private CityDao cityDao;
    private UserCityService userCityService;
    private String signedInUser;

    public CityListAdapter(Context context, List<City> citiesList, UserCityService userCityService, String signedInUser) {
        super(context, 0, citiesList);
        inflater = LayoutInflater.from(context);
        this.userCityService = userCityService;
        this.signedInUser = signedInUser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_city_list, parent, false);
        }

        TextView cityNameView = view.findViewById(R.id.customCityName);
        Button removeButton = view.findViewById(R.id.customRemoveCityButton);
        Button showDetailsButton = view.findViewById(R.id.customCityShowDetailsButton);

        City city = getItem(position);

        if (city != null) {
            cityNameView.setText(city.getCityName());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove from DB.
                    userCityService.removeCityForUser(signedInUser, city.getCityID());

                    remove(city);

                    notifyDataSetChanged();
                }
            });

            showDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        return view;
    }
}
