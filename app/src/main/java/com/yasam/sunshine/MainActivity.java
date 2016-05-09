package com.yasam.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Mon 23/6 - Sunny - 31/17",
                "Tue 24/6 - Foggy - 21/8",
                "Wed 25/6 - Cloudy - 22/17",
                "Thurs 26/6 - Rainy - 18/11",
                "Fri 27/6 - Foggy - 21/10",
                "Sat 28/6 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 29/6 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));
    }
}
