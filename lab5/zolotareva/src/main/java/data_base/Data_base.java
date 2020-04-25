package data_base;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Олеся
 */

public class Data_base {
    ArrayList<String> messages = new ArrayList<String>();
    Connection c;

    void connect() {
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite::memory:");
            System.out.println("Connection established");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Data_base.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
                Logger.getLogger(Data_base.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public ArrayList<String> getMessages() {
        messages.clear();
       
        try {
            Statement stm = c.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM chat ORDER BY id DESC LIMIT 5");
            
            while (res.next()){
                messages.add(res.getString("message"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
    
    public  void add(String message){
        try {
            PreparedStatement pst = c.prepareStatement("INSERT INTO chat messange (?)");
            pst.setString(1, message);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Data_base() {
        try {
            connect();
            
            Statement stm = c.createStatement();
            stm.executeUpdate("CREATE TABLE chat");
        } catch (SQLException ex) {
            Logger.getLogger(Data_base.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
  
   
}
