package com.example.ybarhoush.weatherapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.example.ybarhoush.weatherapp.db.database.AppDatabase;
import com.example.ybarhoush.weatherapp.db.entity.Weather;

import java.util.List;

public class NotifyService extends IntentService {

    //Declare Notification Manager and Notification Listener
    NotificationManager mNotificationManager;
    final int NOTIFICATION_ID = 21651;

    public NotifyService() {
        super("MyNotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.d("MyTestService", "Service running");
        buildNotification();
    }

    public void buildNotification()
    {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String weatherInfo = String.valueOf(getLastWeather(AppDatabase.getAppDatabase(this)));
        String string = weatherInfo;
        String[] parts = string.split("/");
//        for(int i=0;i<parts.length;i++)
//        {
//            Log.d("weatherInfo", i + "..." + parts[i]);
//        }

        // build notification
        Notification notification  = new Notification.Builder(this)
                .setContentTitle(parts[1])
                .setContentText(parts[2])
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true) //clear automatically when clicked
                .build();
        // send notification to notification tray
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    //check if getLastWeather returns the right value
    public static List<Weather> getLastWeather(AppDatabase db)
    {
        List<Weather> weatherList = db.weatherDao().getLast();
//        Log.d("getLastWeather", "Rows Count: " + weatherList.size());
//
//        for(int i=0;i<weatherList.size();i++)
//        {
//            Log.d("getLastWeather", (weatherList.get(i).toString()));
//        }
        return weatherList;
    }
}