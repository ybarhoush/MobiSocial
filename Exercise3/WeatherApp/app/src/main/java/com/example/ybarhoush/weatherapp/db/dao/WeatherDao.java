package com.example.ybarhoush.weatherapp.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.ybarhoush.weatherapp.db.entity.Weather;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather")
    List<Weather> getAll();

    @Query("SELECT * FROM weather ORDER BY uid DESC LIMIT 1")
    List<Weather> getLast();

    @Query("SELECT * FROM weather where city_name LIKE :cityName")
    Weather findByCityName(String cityName);

    @Query("SELECT COUNT(*) from weather")
    int countWeathers();

    @Insert
    void insertAll(Weather... weathers);

    @Delete
    void delete(Weather weather);

    @Query("DELETE FROM weather")
    public void nukeTable();

}
