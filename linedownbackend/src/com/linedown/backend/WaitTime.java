package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;

public class WaitTime {

    private String ID;
    private double CurrentWaitTime;
    private int LastUpdated;
    
    public WaitTime() {
    }
    
    public WaitTime (String placeID, int currentWaitTime, int lastUpdated) {
        this.ID = placeID;
        this.CurrentWaitTime = currentWaitTime;
        this.LastUpdated = lastUpdated;
    }
    
    public WaitTime (String placeID) {
        this.ID = placeID;
        setValues();
    }
    
    private void setValues() {
        Connection connection = connectionFactory.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM WaitTimes WHERE ID=\"" + ID + "\"");
            
            if (results.next())
            {
                this.CurrentWaitTime = results.getInt("CurrentWaitTime");
                this.LastUpdated = results.getInt("LastUpdated");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void setID(String placeID) {
        this.ID = placeID;
    }
    
    public void setCurrentWaitTime(double currentWaitTime) {
        this.CurrentWaitTime = currentWaitTime;
    }
    
    public void setLastUpdated(int lastUpdated){
        this.LastUpdated = lastUpdated;
    }  
    
    
    public String getID() {
        return this.ID;
    }
    
    public double getCurrentWaitTime() {
        return this.CurrentWaitTime;
    }
    
    public int getLastUpdated(){
        return this.LastUpdated;
    }

}
