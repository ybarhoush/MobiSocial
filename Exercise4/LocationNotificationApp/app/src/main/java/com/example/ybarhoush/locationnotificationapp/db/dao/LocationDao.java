package com.example.ybarhoush.locationnotificationapp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.ybarhoush.locationnotificationapp.db.entity.Location;
import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location")
    List<Location> getAll();

    @Query("SELECT * FROM location ORDER BY lid DESC LIMIT 1")
    List<Location> getLast();

    @Query("SELECT COUNT(*) from location")
    int countLocations();

    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);

    @Query("DELETE FROM location")
    public void nukeTable();

}
