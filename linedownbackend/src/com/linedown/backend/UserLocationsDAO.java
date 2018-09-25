package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;

public class UserLocationsDAO {
    private Connection connection;
    
    public UserLocationsDAO () {
        connection = connectionFactory.getConnection();
    }

    UserLocations getUserLocations(String ID) {
         UserLocations locations = new UserLocations();
         try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM UserLocations WHERE ID=\"" + ID + "\"");
            
            
            if (results.next()) {
                locations.setID(ID);
                locations.setPeopleNearby(results.getInt("PeopleNearby"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return locations;
    }

    boolean insertUserLocations(String ID, int PeopleNearby) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("INSERT INTO UserLocations (ID, PeopleNearby) VALUES (\"" + ID + "\", " + PeopleNearby + ")");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean updateUserLocations(UserLocations u, int PeopleNearby) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("UPDATE UserLocations SET PeopleNearby=" + PeopleNearby + " WHERE  ID=\"" + u.getID() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean deleteUserLocations(UserLocations u) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("DELETE FROM UserLocations WHERE  ID=\"" + u.getID() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    
    }
}
