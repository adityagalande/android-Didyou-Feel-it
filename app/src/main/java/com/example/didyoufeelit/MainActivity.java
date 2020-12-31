package com.example.didyoufeelit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_REQ_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQ_URL);

    }

    private void updateUI(Event earthquake) {

        TextView titleview = (TextView) findViewById(R.id.title);
        titleview.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }

//    private int getString(int num_people_felt_it, String numOfPeople) {
//    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, Event> {

        @Override
        protected Event doInBackground(String... urls) {
            if (urls.length > 0 || urls[0] == null) {
                return null;
            }

            return Utils.fetchEarthquakeData(urls[0]);
        }

        @Override
        protected void onPostExecute(Event event) {
            updateUI(event);
        }
    }
}