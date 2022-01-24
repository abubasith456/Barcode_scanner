package com.example.grootan.models;

public class ScannedData {

    String id, date, time, scanned_data;

    public ScannedData() {

    }

    public ScannedData(String id, String date, String time, String scanned_data) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.scanned_data = scanned_data;
    }

    //Encapsulation

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScanned_data() {
        return scanned_data;
    }

    public void setScanned_data(String scanned_data) {
        this.scanned_data = scanned_data;
    }
}
