
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class DataBase {
    Connection connection;
            
    DataBase() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String path = "./data.db";
        connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        Statement statem = connection.createStatement();
        try {
            statem.executeUpdate("CREATE TABLE messagehistory (id INTEGER PRIMARY KEY AUTOINCREMENT, letter VARCHAR(100));");
            System.out.println("Connect to new DB: " + path);
        }
        catch (SQLException ex) {
            Logger.getLogger(/*DataBase.class.getName()).log(Level.SEVERE, null, ex*/" connect to " +path);
        }
        //System.out.println("Connection to DB: success");
    }
    
    String getHistory() throws SQLException {
        Statement statem = connection.createStatement();
        ResultSet res = statem.executeQuery("SELECT * FROM (SELECT * FROM messagehistory ORDER BY id DESC LIMIT 5) ORDER BY id ASC;");
        String history = "";
        while (res.next()) {
            history += res.getString("letter") + "\n";
        }
        return history;
    }
    
    void UpdateHistory(String mes) throws SQLException
    {
        PreparedStatement statem = connection.prepareStatement("INSERT INTO messagehistory (letter) VALUES (?);");
        statem.setString(1, mes);
        statem.executeUpdate();
    }
}
