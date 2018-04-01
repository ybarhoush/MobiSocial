package com.example.ybarhoush.locationnotificationapp.db;

import android.util.Log;

import com.example.ybarhoush.locationnotificationapp.db.database.AppDatabase;
import com.example.ybarhoush.locationnotificationapp.db.entity.Location;

import java.util.List;

public class DatabaseInitializer {


    private static Location addLocation(final AppDatabase db, Location location)
    {
        db.locationDao().insertAll(location);
        return location;
    }

    public static List<Location> getAllLocations(AppDatabase db)
    {
        List<Location> locationList = db.locationDao().getAll();

        Log.d("dbDebug", "Rows Count: " + locationList.size());
        for(int i=0;i<locationList.size();i++)
        {
            Log.d("dbDebug", String.valueOf(locationList.get(i)));
        }

        return locationList;
    }

    public static void populateLocation(AppDatabase db, String longitude, String latitude, String msg)
    {
        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setMsg(msg);
        addLocation(db, location);

        List<Location> locationList = db.locationDao().getAll();

        Log.d("dbDebug", "Rows Count: " + locationList.size());
        for(int i=0;i<locationList.size();i++)
        {
            Log.d("dbDebug", String.valueOf(locationList.get(i)));
        }
    }

    public static void deleteLocationTable(AppDatabase db)
    {
        db.locationDao().nukeTable();
    }
}
