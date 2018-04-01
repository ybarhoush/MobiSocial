package com.example.ybarhoush.locationnotificationapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Location")

public class Location {
    @Override
    public String toString() {
        StringBuffer buffer= new StringBuffer();

        buffer.append("Longitude is ");

        buffer.append(this.longitude);

        buffer.append(" ");
        buffer.append("Latitude is ");

        buffer.append(this.latitude);

        buffer.append(" / ");
        buffer.append(this.msg);
        buffer.append(" /");
        return buffer.toString();
    }

    @PrimaryKey(autoGenerate = true)
    private int lid;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "msg")
    private String msg;

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }
}