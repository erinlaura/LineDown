package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;

public class WaitTimeDAO {
    private Connection connection;
    
    public WaitTimeDAO () {
        connection = connectionFactory.getConnection();
    }

    WaitTime getWaitTime(String ID) {
         WaitTime wait = new WaitTime();
         try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM WaitTimes WHERE ID=\"" + ID + "\"");
            
            
            while (results.next()) {
                wait.setID(ID);
                wait.setCurrentWaitTime(results.getDouble("CurrentWaitTime"));
                wait.setLastUpdated(results.getInt("LastUpdated"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return wait;
    }

    boolean insertWaitTime(String ID, double CurrentWaitTime, int LastUpdated) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("INSERT INTO WaitTimes (ID, CurrentWaitTime, LastUpdate) VALUES (\"" + ID + "\", " + CurrentWaitTime + ", " + LastUpdated + ")");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean updateWaitTime(WaitTime w, double CurrentWaitTime, int LastUpdated) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("UPDATE WaitTimes SET CurrentWaitTime=" + CurrentWaitTime + ", LastUpdated=" + LastUpdated + " WHERE  ID=\"" + w.getID() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean deleteWaitTime(WaitTime w) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("DELETE FROM WaitTimes WHERE  ID=\"" + w.getID() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    
    }
}
