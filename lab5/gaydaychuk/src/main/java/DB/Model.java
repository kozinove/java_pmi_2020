package DB;

import java.sql.*;
import java.util.ArrayList;

public class Model {
    ArrayList<Message> list = new ArrayList<Message>();
    Connection c;

    private void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:memory");

            System.out.println("Connected to DB");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Model() {
        connect();
    }

    public ArrayList<Message> getList() {
        list.clear();
        try {
            Statement stm = c.createStatement();
            ResultSet r = stm.executeQuery("select * from message_table order by id DESC LIMIT 5");
            while (r.next()){
                list.add(new Message(r.getInt("id"), r.getString("sender"), r.getString("text")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public  void add(Message m){
        try {
            PreparedStatement ps = c.prepareStatement("insert into message_table (sender, text) VALUES (?, ?)");
            ps.setString(1, m.getSenderId());
            ps.setString(2, m.getText());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        list.add(m);
    }
}
