package Database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDatabase {
    Connection con;

    public MyDatabase() {
        try {
            // try connect to database
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite::memory:");
            // create a table
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE TABLE messages (id INTEGER PRIMARY KEY AUTOINCREMENT, nickname VARCHAR (20), message VARCHAR (200))");
            stm.executeUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, nickname VARCHAR (20))");
            System.out.append("Database is connected\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String name, String message){
        try {
            // add message to database
            PreparedStatement pst = con.prepareStatement("INSERT INTO messages (nickname, message) VALUES (?, ?)");
            pst.setString(1, name);
            pst.setString(2, message);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String name){
        try {
            // add user to database
            PreparedStatement pst = con.prepareStatement("INSERT INTO users (nickname) VALUES (?)");
            pst.setString(1, name);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void delete(String name){
        try {
            // add user to database
            PreparedStatement pst = con.prepareStatement("DELETE FROM users where (nickname) = (?)");
            pst.setString(1, name);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMessages() {
        String messages = "";
        try {
            Statement stm = con.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM (SELECT * FROM messages ORDER BY id DESC) ORDER BY id ASC");
            while (res.next()) {
                // add messages to string
                messages += "[" + res.getString("nickname") + "]: ";
                messages += res.getString("message") + '\n';
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }

    public String getUsers() {
        String messages = "";
        try {
            // take 10 last messages in database
            Statement stm = con.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM (SELECT * FROM users ORDER BY nickname DESC) ORDER BY nickname ASC");
            while (res.next()) {
                // add messages to string
                messages += res.getString("nickname") + '\n';
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
}
