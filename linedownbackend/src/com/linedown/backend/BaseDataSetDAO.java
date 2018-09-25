package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;

public class BaseDataSetDAO {
    private Connection connection;
    
    public BaseDataSetDAO () {
        connection = connectionFactory.getConnection();
    }

    BaseDataSet getBaseDataSet(String ID, String DayOfWeek, int TimeOfDay) {
         BaseDataSet bds = new BaseDataSet();
         try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM BaseDataSets WHERE ID=\"" + ID + DayOfWeek + TimeOfDay + "\"");
            
            
            if (results.next()) {
                bds.setID(ID);
                bds.setDayOfWeek(DayOfWeek);
                bds.setTimeOfDay(TimeOfDay);
                bds.setWaitTime(results.getInt("WaitTime"));
                bds.setAvgPeopleNearby(results.getDouble("AvgPeopleNearby"));
                bds.setDayTrackedSince(results.getInt("DayTrackedSince"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bds;
    }

    boolean insertBaseDataSet(String ID, String DayOfWeek, int TimeOfDay, double WaitTime, double AvgPeopleNearby, int DayTrackedSince) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("INSERT INTO BaseDataSets (ID, PlaceID, DayOfWeek, TimeOfDay, WaitTime, AvgPeopleNearby, DayTrackedSince) VALUES (\"" + ID + DayOfWeek + TimeOfDay + "\", \"" + ID + "\",\"" + DayOfWeek + "\"," + TimeOfDay + "," + WaitTime + "," + AvgPeopleNearby + "," + "," + DayTrackedSince + ")");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean updateBaseDataSet(BaseDataSet b, double WaitTime, double AvgPeopleNearby) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("UPDATE BaseDataSets SET WaitTime=" + WaitTime + ", AvgPeopleNearby=" + AvgPeopleNearby + " WHERE  ID=\"" + b.getID() + b.getDayOfWeek() + b.getTimeOfDay() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean deleteBaseDataSet(BaseDataSet b) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("DELETE FROM BaseDataSets WHERE  ID=\"" + b.getID() + b.getDayOfWeek() + b.getTimeOfDay() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    
    }
}
