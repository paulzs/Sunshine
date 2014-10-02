package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Paul Shi on 9/15/14.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Some Fake Data
        /*final String[] forecastArray = {
                "Today -- Sunny -- 88/63",
                "Tomorrow -- Foggy -- 70/40",
                "Weds -- Cloudy -- 72/63",
                "Thurs -- Asteroids -- 75/65",
                "Fri -- Meatballs -- 65/56",
                "Sat -- HELP THE BRITISH ARE COMING -- 60/51",
                "Sun -- Sunny -- 80/68"
        };*/

        // Make an arraylist with data
        //List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        //Create ArrayAdapter to take data and populate ListView
        forecastAdapter = new ArrayAdapter<String>(
                // context
                getActivity(),
                // ID of list item layout
                R.layout.list_item_forecast,
                // ID of textview
                R.id.list_item_forecast_textview,
                // Data
                new ArrayList<String>()
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = forecastAdapter.getItem(position);
                Intent detailActIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(detailActIntent);


            }
        });

        return rootView;
    }

    private void updateWeather(){
        String location = Utility.getPreferredLocation(getActivity());
        new FetchWeatherTask(getActivity(), forecastAdapter).execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

}