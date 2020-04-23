import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class MyServer {
    private static Connection con;
    private static String curr_quest;
    ArrayList<ClientThread> clients = new ArrayList<>();
    int yes_counter = 0;
    int no_counter = 0;

    public static void set_curr_quest(String quest){
        curr_quest = quest;
    }
    public static void create_new_database() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite::memory:");
            Statement stm = con.createStatement();
            stm.executeUpdate("CREATE TABLE voting" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "question VARCHAR (200)," +
                    "Yes INTEGER," +
                    "No INTEGER)");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static String get_last() {
        String result = "";
        try {
            ResultSet r = con.createStatement().executeQuery("SELECT * FROM voting ORDER BY id ASC LIMIT 5");
            while (r.next()){
                result += (r.getString("question")+" ");
                result += r.getInt("Yes") + " ";
                result += r.getInt("No");
                result += "\n";
            }
        } catch (SQLException ex) {
            System.out.append("SQL Error occured.\n");
        }
        return result;
    }

    public static void add_row_to_db(String quest, int yes, int no){
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO voting" +
                    " (question, yes, no) VALUES (?,?,?)");
            pst.setString(1, quest);
            pst.setInt(2, yes);
            pst.setInt(3, no);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void bcast(String s) {
        for(ClientThread client : clients) {
            client.send(s);
        }
    }

    public void end_vote(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String res = gson.toJson(new Message("RESULT","+ : " + Integer.toString(yes_counter)
                + "\n - : " + Integer.toString(no_counter)+"\n"));
        add_row_to_db(curr_quest,yes_counter,no_counter);
        yes_counter = no_counter = 0;
        bcast(res);
    }
    synchronized public void vote(String s) {
        if(s.equals("+"))
            ++yes_counter;
        else
            ++no_counter;
        if(yes_counter + no_counter == clients.size())
            end_vote();
    }
    public void start_server() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (ServerSocket server=new ServerSocket(3345)){
            create_new_database();
            while(true){
                Socket  cs = server.accept();
                clients.add(new ClientThread(cs, this));
                System.out.append("Client #" + Integer.toString(clients.size()) + " connected.\n");
                Message msg = new Message("HISTORY", get_last());
                String s = gson.toJson(msg);
                clients.get(clients.size() - 1).send(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        MyServer s = new MyServer();
        s.start_server();
    }
}