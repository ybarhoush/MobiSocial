package com.example.ybarhoush.weatherapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "weather")
public class Weather {
    @Override
    public String toString() {
        StringBuffer buffer= new StringBuffer();
        buffer.append("/Weather Status in ");
        buffer.append(this.cityName);
        buffer.append("/Temperature is ");
        buffer.append(this.temp);
        buffer.append(", and Condition is ");
        buffer.append(this.weatherCondition);
        buffer.append("/");
        return buffer.toString();
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "city_name")
    private String cityName;

    @ColumnInfo(name = "weather_condition")
    private String weatherCondition;

    @ColumnInfo(name = "temp")
    private String temp;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

}

