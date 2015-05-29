/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package databasejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author c0645457
 */
public class Databasejava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        // TODO code application logic here
       try{
           Class.forName("com.ibm.as400.access.AS400JDBCDriver");
       }
        catch(ClassNotFoundException e){
                    System.out.println("Class not found exception! " + 
                e.getMessage());

        }
        
        
        String url ="jdbc:as400://174.79.32.158";
        try {
            Connection con = DriverManager.getConnection(url, 
                                       "ibm69", "ibm69");
            // Insert Stuff to do Here!
            
            
            
            String query="select * from BOOKS";
        /*Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query); */
             PreparedStatement pstmt = con.prepareStatement(query);
         int[] idList = {1, 3};
            for (int id : idList) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
          while (rs.next()) {
                System.out.printf("%s\t%s\n", rs.getString("BOOK_CODE"), rs.getString("TITLE"));
            }        
            }
        con.close();
        }
        catch (SQLException e) {
            System.out.println("SQL Issue! " + e.getMessage());
            e.printStackTrace();
        }


    }
    
}
