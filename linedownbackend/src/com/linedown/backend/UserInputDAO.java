package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.*;

public class UserInputDAO {

    private Connection connection;
    
    public UserInputDAO () {
        connection = connectionFactory.getConnection();
    }

    List<UserInput> getUserInputFor(String ID) {
        List<UserInput> inputs = new ArrayList<UserInput>();
    
         try {
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM UserInput WHERE PlaceID=\"" + ID + "\" ORDER BY TimeInput");
            
            
            while (results.next()) {
                UserInput input = new UserInput();
                
                input.setID(ID);
                input.setWaitTime(results.getInt("WaitTime"));
                input.setTimeInput(results.getInt("TimeInput"));
                
                inputs.add(input);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return inputs;
    }

    boolean insertUserInput(String PlaceID, int WaitTime, int TimeInput) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("INSERT INTO UserInput (ID, PlaceID, WaitTime, TimeInput) VALUES (\"" + PlaceID + TimeInput + WaitTime + "\", \"" + PlaceID + "\"," + WaitTime + ", " + TimeInput + ")");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean updateUserInput(UserInput u, int WaitTime, int TimeInput) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("UPDATE UserInput SET ID=\"" + u.getID() + TimeInput + WaitTime + "\", WaitTime=" + WaitTime + ", TimeInput=" + TimeInput + " WHERE  ID=\"" + u.getID() + u.getTimeInput() + u.getWaitTime() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    boolean deleteUserInput(UserInput u) {
        try {
            Statement stmt = connection.createStatement();
            int success = stmt.executeUpdate("DELETE FROM UserInput WHERE  ID=\"" + u.getID() + u.getTimeInput() + "\"");
            
            if (success == 1) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    
    }
}
