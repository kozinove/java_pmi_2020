/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.mychat.message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J-win
 */
public class JMessageDB {
    Connection con;

    public JMessageDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:.\\src\\main\\java\\chat\\mychat\\message\\Messages.db");
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR (200))");
            System.out.append("=== Database is connected ===\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JMessageDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JMessageDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String message){
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO history (message) VALUES (?)");
            pst.setString(1, message);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JMessageDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMessages() {
        String messages = "";
        try {
            Statement stm = con.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM (SELECT * FROM history ORDER BY id DESC LIMIT 5) ORDER BY id ASC");
            while (res.next()) {
                messages += res.getString("message") + '\n';
            }
        } catch (SQLException ex) {
            Logger.getLogger(JMessageDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
}
