package edu.uiuc.cs427app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CityListAdapter extends ArrayAdapter<City> {
    private LayoutInflater inflater;

    public CityListAdapter(Context context, List<City> citiesList) {
        super(context, 0, citiesList);
        inflater = LayoutInflater.from(context);
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
            cityNameView.setText(city.cityName);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(city);
                    // Call to DB to remove the city for the user here.
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
