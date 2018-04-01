package com.example.ybarhoush.weatherapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybarhoush.weatherapp.db.database.AppDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.ybarhoush.weatherapp.db.DatabaseInitializer.populateWeatherWithAPI;

public class WeatherAppController extends AppCompatActivity {

    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "2e61cb0e9b2ec62ce9f0fc07c8bba7e5";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 100; //identify and track our permission request
    // Set LOCATION_PROVIDER
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    //String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    // Member Variables:
    TextView mTemperatureLabel;
    TextView mWeatherLabel;
    TextView mCityLabel;

    // Declare a LocationManager and a LocationListener
    LocationManager mLocationManager; // start/stop requesting location updates
    LocationListener mLocationListener; //notify if location changed

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app_controller);

        // Linking the elements in the layout to Java code
        mTemperatureLabel = findViewById(R.id.textView_temperatureLabel);
        mWeatherLabel = findViewById(R.id.textView_weatherLabel);
        mCityLabel = findViewById(R.id.textView_CityLabel);

        //DatabaseInitializer.populateWeatherTest(AppDatabase.getAppDatabase(this));
        //DatabaseInitializer.deleteWeather(AppDatabase.getAppDatabase(this));

        String currentDBPath=getDatabasePath("user-database").getAbsolutePath();
        Log.d("dbDebug", currentDBPath);

        scheduleAlarm();
    }

    // Setup a recurring alarm every 1min
    public void scheduleAlarm() {
        long INTERVAL_TEN_SECOND =  AlarmManager.INTERVAL_FIFTEEN_MINUTES/150;
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, INTERVAL_TEN_SECOND, pIntent);
    }

    // onResume()
    // happens after onCreate and before user interact w/ activity
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DebugApp", "onResume() called");
        Log.d("DebugApp", "Getting weather for current location");
        getWeatherForCurrentLocation();

    }

    private void getWeatherForCurrentLocation()
    {
        // Get hold of location manager and assign to mLocation
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Start mLocationListener
        mLocationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                Log.d("DebugApp", "onLocationChanged");

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                Log.d("DebugApp", longitude);
                Log.d("DebugApp", latitude);

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);

                doNetworking(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {
                Log.d("DebugApp", "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String s)
            {
                Log.d("DebugApp", "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String s)
            {
                Log.d("DebugApp", "onProviderDisabled");
            }
        };

        //Permission Check!
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Request Permission >> ActivityCompat#requestPermissions
            // Act on User's Response
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        Log.d("DebugApp", "requestLocationUpdates");
    }

// ------------------- check if the requestCode in callback== REQUEST_CODE
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //does the requestCode in callback== REQUEST_CODE
//        if (requestCode == REQUEST_CODE) {
//            // response check
//            //grantResults has more than 1 element
//            //and grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d("DebugApp", "Permission was granted");
//                getWeatherForCurrentLocation();
//            } else {
//                Log.d("DebugApp", "Permission was denied");
//            }
//        }
//    }


    //make an HTTP request
    private void doNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        Log.d("DebugApp", "onSucces HTTP request");
                        Log.d("DebugApp", String.valueOf(response));
                        try
                        {
                            WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                            updateUI(weatherData);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response)
                    {
                        Log.d("DebugApp", "onFailure HTTP request");
                        Log.e("DebugApp", String.valueOf(e));
                        Toast.makeText(WeatherAppController.this, "HTTP request fail", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //updateUI()
    //and push to table
    public void updateUI(WeatherDataModel weather)
    {
        mTemperatureLabel.setText(weather.getmTemperature());
        mWeatherLabel.setText(weather.getmWeatherStatus());
        mCityLabel.setText(weather.getmCity());

        //and push to table
        populateWeatherWithAPI(AppDatabase.getAppDatabase(this), weather.getmCity(), weather.getmWeatherStatus(), weather.getmTemperature());
    }
}
