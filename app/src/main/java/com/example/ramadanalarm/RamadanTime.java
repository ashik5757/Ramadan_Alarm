package com.example.ramadanalarm;

import java.sql.Time;
import java.util.Date;

public class RamadanTime {

    public int id;
    public String date;
    public String ifterTime;
    public String sehriTime;

    public RamadanTime() {
    }

    public RamadanTime(int id, String date, String ifterTime, String sehriTime) {
        this.id = id;
        this.date = date;
        this.ifterTime = ifterTime;
        this.sehriTime = sehriTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIfterTime() {
        return ifterTime;
    }

    public void setIfterTime(String ifterTime) {
        this.ifterTime = ifterTime;
    }

    public String getSehriTime() {
        return sehriTime;
    }

    public void setSehriTime(String sehriTime) {
        this.sehriTime = sehriTime;
    }

}
