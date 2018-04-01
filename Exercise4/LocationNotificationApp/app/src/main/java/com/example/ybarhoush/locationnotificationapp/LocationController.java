package com.example.ybarhoush.locationnotificationapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybarhoush.locationnotificationapp.db.DatabaseInitializer;
import com.example.ybarhoush.locationnotificationapp.db.dao.LocationDao;
import com.example.ybarhoush.locationnotificationapp.db.database.AppDatabase;

import java.util.List;

public class LocationController extends AppCompatActivity {

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 100; //identify and track our permission request
    // Set LOCATION_PROVIDER
    //String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    // Declare a LocationManager and a LocationListener
    LocationManager mLocationManager; // start/stop requesting location updates
    LocationListener mLocationListener; //notify if location changed

    public static String longitude = "1000";
    public static String latitude = "1000";
    public static String msgString = "No Msg Was Stored";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_controller);

        getCurrentLocation();

        String currentDBPath=getDatabasePath("user-database").getAbsolutePath();
        Log.d("dbDebug", currentDBPath);

        scheduleAlarm();
    }

    // Setup a recurring alarm every 1min for notification
    public void scheduleAlarm() {
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
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES/1500, pIntent);
    }

    public void storeLocationAndMsg(View view) {
        //DatabaseInitializer.populateLocationTest(AppDatabase.getAppDatabase(this));
        EditText msg = findViewById(R.id.editText_msg);
        msgString = msg.getText().toString();
        msg.setText("");

        DatabaseInitializer.populateLocation(AppDatabase.getAppDatabase(this), longitude, latitude, msgString);
        Toast.makeText(getApplicationContext(), "Location + Msg were Stored", Toast.LENGTH_SHORT).show();
    }

    public void emptyLocationTable(View view) {
        DatabaseInitializer.deleteLocationTable(AppDatabase.getAppDatabase(this));
        Toast.makeText(getApplicationContext(), "Locations were Deleted", Toast.LENGTH_SHORT).show();
    }

    private void getCurrentLocation()
    {
        // Get hold of location manager and assign to mLocation
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Start mLocationListener
        mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location)
            {
                Log.d("Debug Location", "onLocationChanged");

                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());

                Log.d("Debug Location", longitude);
                Log.d("Debug Location", latitude);

                TextView longitudeTextView = findViewById(R.id.textView1);
                longitudeTextView.setText(longitude);

                TextView latitudeTextView = findViewById(R.id.textView);
                latitudeTextView.setText(latitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {
                Log.d("Debug Location", "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String s)
            {
                Log.d("Debug Location", "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String s)
            {
                Log.d("Debug Location", "onProviderDisabled");
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
        Log.d("Debug Location", "requestLocationUpdates");
    }
}