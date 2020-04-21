package Database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDatabase {
    String messages;
    Connection con;

    public MyDatabase() {
        try {
            // try connect to database
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite::memory:");
            // create a table
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE TABLE clientserver (id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR (200))");
            System.out.append("=== Database is connected ===\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String message){
        try {
            // add message to database
            PreparedStatement pst = con.prepareStatement("INSERT INTO clientserver (message) VALUES (?)");
            pst.setString(1, message);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMessages() {
        messages = "";
        try {
            // take 5 last messages in database
            Statement stm = con.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM (SELECT * FROM clientserver ORDER BY id DESC LIMIT 5) ORDER BY id ASC");
            while (res.next()) {
                // add messages to string
                messages += res.getString("message") + '\n';
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
}
