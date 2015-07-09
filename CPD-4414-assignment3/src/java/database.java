/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author c0645457
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class database{
    public static Connection getConnection() throws SQLException {

         String jdbc = "jdbc:mysql://IPRO/Winter2015";
            String user = "Winter2015";
            String pass = "P@ssw0rd";
            

        Connection conn = null;
        try {
            Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not found  exception" + e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(jdbc, user, pass);
        } catch (SQLException e) {
            System.out.println("SQL Error found " + e.getMessage());
        }
        return conn;
    }
    
}
