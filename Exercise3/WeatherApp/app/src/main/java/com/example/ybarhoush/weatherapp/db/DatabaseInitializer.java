package com.example.ybarhoush.weatherapp.db;

import android.util.Log;

import com.example.ybarhoush.weatherapp.db.database.AppDatabase;
import com.example.ybarhoush.weatherapp.db.entity.Weather;

import java.util.List;

public class DatabaseInitializer {


    private static Weather addWeather(final AppDatabase db, Weather weather)
    {
        db.weatherDao().insertAll(weather);
        return weather;
    }

    public static void populateWeatherTest(AppDatabase db)
    {
        Weather weather = new Weather();
        weather.setCityName("Oulu");
        weather.setWeatherCondition("Snowy");
        weather.setTemp("-2");
        addWeather(db, weather);

        List<Weather> weatherList = db.weatherDao().getAll();

        Log.d("dbDebug", "Rows Count: " + weatherList.size());
        for(int i=0;i<weatherList.size();i++)
        {
            Log.d("dbDebug", String.valueOf(weatherList.get(i)));
        }
    }

    public static void populateWeatherWithAPI(AppDatabase db, String cityName, String weatherCond, String temp)
    {
        Weather weather = new Weather();
        weather.setCityName(cityName);
        weather.setWeatherCondition(weatherCond);
        weather.setTemp(temp);
        addWeather(db, weather);

        List<Weather> weatherList = db.weatherDao().getAll();

        Log.d("dbDebug", "Rows Count: " + weatherList.size());
        for(int i=0;i<weatherList.size();i++)
        {
            Log.d("dbDebug", String.valueOf(weatherList.get(i)));
        }
    }

    public static void deleteWeather(AppDatabase db)
    {
        db.weatherDao().nukeTable();
    }
}
