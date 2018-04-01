package com.example.ybarhoush.locationnotificationapp;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.ybarhoush.locationnotificationapp.db.DatabaseInitializer;
import com.example.ybarhoush.locationnotificationapp.db.database.AppDatabase;
import com.example.ybarhoush.locationnotificationapp.db.entity.Location;

import java.util.List;

public class NotifyService extends IntentService {

    //Declare Notification Manager and Notification Listener
    NotificationManager mNotificationManager;
    final int NOTIFICATION_ID = 21651;
    double distance;
    public NotifyService() {
        super("MyNotifyService");
    }
    String msgAtIndex = "Empty";

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here

        Log.d("MyTestService", "************** **Service running ********************** ");
        //getCurrentLocation();

        String longitude = LocationController.longitude;
        String latitude = LocationController.latitude;
        Log.d("MyTestService-long", longitude);
        Log.d("MyTestService-lat", latitude);

        // Get All Locations
        List<Location> allStoredLocations
                = DatabaseInitializer.getAllLocations(AppDatabase.getAppDatabase(this));

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        // Iterate Through Locations
        for(int i=0;i<allStoredLocations.size();i++)
        {
            Log.d("MyTestService-Index", "== Compare to INDEX IN DB = "+ i +" ==");
            // get Lang and Lat for Location at Index
            float longitudeAtIndex = Float.parseFloat(allStoredLocations.get(i).getLongitude());
            Log.d("MyTestService-longAtI", String.valueOf(longitudeAtIndex));
            float latitudeAtIndex = Float.parseFloat(allStoredLocations.get(i).getLatitude());
            Log.d("MyTestService-latAtI", String.valueOf(latitudeAtIndex));

            msgAtIndex = allStoredLocations.get(i).getMsg();
            Log.d("MyTestService-seeMsg", msgAtIndex);

            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);

            // calc distance between location at index and current location
            distance = distance(lat, lon, latitudeAtIndex, longitudeAtIndex, "K");
            Log.d("MyTestService-DCalc", String.valueOf(distance));

            // if distance is smaller than 2 Km, notify user
            if (distance < 2) {
                Log.d("MyTestService-match?", "Match!");

                // build notification
                notification  = new Notification.Builder(this)
                        .setContentTitle(msgAtIndex)
                        .setContentText("Distance is " + String.valueOf(distance))
                        .setSmallIcon(R.drawable.icon)
                        .setAutoCancel(true) //clear automatically when clicked
                        .build();
                // send notification to notification tray
                notificationManager.notify(NOTIFICATION_ID, notification);
                break;
                } else {
                notificationManager.cancelAll();
                Log.d("MyTestService-noMsg!", "No Match, hide Notification");
            }
        }
    }

    //Distance Calc code from https://www.geodatasource.com/developers/java
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;

        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}