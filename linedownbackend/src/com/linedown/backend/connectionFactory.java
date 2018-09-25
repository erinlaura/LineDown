package com.linedown.backend;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class connectionFactory {
    
    private static final String URL = "jdbc:mysql://localhost:3306/LineDown";
    private static final String USER = "root";//"jdyoung";
    private static final String PW = "JD";//"LineDown5895";
    
    public static Connection getConnection() {
    //    if (System.getProperty("RDS_HOSTNAME") != null) {
            try {
              Class.forName("com.mysql.jdbc.Driver");
    /*          String dbName = System.getProperty("RDS_DB_NAME");
              String userName = System.getProperty("RDS_USERNAME");
              String password = System.getProperty("RDS_PASSWORD");
              String hostname = System.getProperty("RDS_HOSTNAME");
              String port = System.getProperty("RDS_PORT");
              String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
              Connection con = DriverManager.getConnection(jdbcUrl);
              return con;
            }
            catch (ClassNotFoundException e) { e.printStackTrace(); }
            catch (SQLException e) { e.printStackTrace(); }
            }
            return null;
          }
  
    */    
           } catch (ClassNotFoundException e) {
            throw new RuntimeException("ERROR: Class com.mysql.jdbc.Driver not found", e);
        }
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(URL, USER, PW);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Database Connection Failed", e);
        }
    }
    /**/
        
}
