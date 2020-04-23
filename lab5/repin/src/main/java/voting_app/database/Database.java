/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting_app.database;

import java.sql.*;
/**
 * 
 * @author mezotaken
 */
public class Database {
    Connection c;
    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite::memory:");
            c.createStatement().executeUpdate("CREATE TABLE events (id INTEGER PRIMARY KEY AUTOINCREMENT, event VARCHAR (100))");
        } catch (ClassNotFoundException ex) {
            System.out.append("DB Class not found.\n");
        } catch (SQLException ex) {
            System.out.append("SQL Error occured.\n");
        }
        System.out.append("Database created.\n");
    }
    public void AddEvent(String s) {
        try {
            c.createStatement().executeUpdate("INSERT INTO events (event) VALUES (\"" + s + "\")");
        } catch (SQLException ex) {
            System.out.append("SQL Error occured.\n");
        }
    }
    public String GetRecentHistory() {
        String result = "";
        try {
            ResultSet r = c.createStatement().executeQuery("SELECT * FROM events ORDER BY id ASC LIMIT 5");
            while (r.next()){
                result += (r.getString("event")+"\n----------\n");
            }
        } catch (SQLException ex) {
            System.out.append("SQL Error occured.\n");
        }
        return result;  
    }
}
