package com.example.ybarhoush.weatherapp;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

//data management and manipulation goes here
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;


public class WeatherDataModel {

    // Declare the member variables here
    private static String mTemperature;

    private static String mWeatherStatus;

    private static String mCity;

    // Parse the JSON and Handle Exceptions with a Try-Catch Block
    // Create a WeatherDataModel from a JSON:
    public static WeatherDataModel fromJson(JSONObject jsonObject) throws JSONException {
        WeatherDataModel weatherData = new WeatherDataModel();

        weatherData.mCity = jsonObject.getString("name");

        //  JSONarray to list and extract "description" value
        String string = mWeatherStatus = String.valueOf(jsonObject.getJSONArray("weather"));
        String[] parts = string.split("\"");
//        for(int i=0;i<parts.length;i++)
//        {
//            Log.d("JSONarray", i + "..." + parts[i]);
//        }
        weatherData.mWeatherStatus = parts[5]+", "+parts[9];

        double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
        int roundValue = (int) Math.rint(tempResult);
        weatherData.mTemperature = String.valueOf(roundValue);

        return weatherData;

    }



    // Create getter methods for temperature, city, and icon name
    public static String getmTemperature() {
        return mTemperature + "°";
    }
    public static String getmWeatherStatus() {
        return mWeatherStatus;
    }
    public static String getmCity() {
        return mCity;
    }
}
