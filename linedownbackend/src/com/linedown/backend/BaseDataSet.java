package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;


public class BaseDataSet {

    private String ID;
    private String PlaceID;
    private String DayOfWeek;
    private int TimeOfDay;
    private double WaitTime;
    private double AvgPeopleNearby;
    private int DayTrackedSince;
    
    public BaseDataSet() {
    }
    
    public BaseDataSet (String placeID, String dayOfWeek, int timeOfDay, double WaitTime, double AvgPeopleNearby, int DayTrackedSince) {
        this.ID = placeID + dayOfWeek + timeOfDay; 
        this.PlaceID = placeID;
        this.DayOfWeek = dayOfWeek;
        this.TimeOfDay = timeOfDay;
        this.WaitTime = WaitTime;
        this.AvgPeopleNearby = AvgPeopleNearby;
        this.DayTrackedSince = DayTrackedSince;
    }

    /*private void setValues() {
        Connection connection = connectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM BaseDataSets WHERE ID=\"" + ID + "\" AND DayOfWeek=\"" + DayOfWeek + "\" AND TimeOfDay=" + TimeOfDay);
            
            if (results.next())
            {
                this.WaitTime = results.getInt("WaitTime");
                this.DayTrackedSince = results.getInt("DayTrackedSince");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*//**/
    
    public void setID(String placeID) {
        this.PlaceID = placeID;
        this.ID = this.PlaceID + this.DayOfWeek + String.valueOf(this.TimeOfDay);
    }
    
    public void setDayOfWeek(String dayOfWeek) {
        this.DayOfWeek = dayOfWeek;
        this.ID = this.PlaceID + this.DayOfWeek + String.valueOf(this.TimeOfDay);
    }
    
    public void setTimeOfDay(int timeOfDay) {
        this.TimeOfDay = timeOfDay;
        this.ID = this.PlaceID + this.DayOfWeek + String.valueOf(this.TimeOfDay);
    }
    
    public void setWaitTime(double waitTime) {
        this.WaitTime = waitTime;
    }
    
    public void setAvgPeopleNearby(double avgPeopleNearby) {
        this.AvgPeopleNearby = avgPeopleNearby;
    }
    
    public void setDayTrackedSince(int dayTrackedSince) {
        this.DayTrackedSince = dayTrackedSince;
    }
    
    public String getID() {
        return this.PlaceID;
    }

    public String getDayOfWeek() {
        return this.DayOfWeek;
    }
    
    public int getTimeOfDay() {
        return this.TimeOfDay;
    }
    
    
    public double getWaitTime() {
        return this.WaitTime;
    }
    
    public double getAvgPeopleNearby() {
        return this.AvgPeopleNearby;
    }
    
    public int getDayTrackedSince(){
        return this.DayTrackedSince;
    }
    
}

