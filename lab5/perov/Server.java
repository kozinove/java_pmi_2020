/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author perov
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

class myClient extends Thread {

    private static Gson gson;

    private static ArrayList<String> story = new ArrayList<>();

    int id;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    Message from_to_me;

    boolean i_voted = false;
    private static boolean voting = false;
    boolean itsme = false;

    private static int yes = 0;
    private static int no = 0;
    private static boolean flag = false;
    private static int count = 0;

    Connection c;

    void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\STUFF\\PROGRAMMING\\JAVA\\Chat.db");
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ArrayList<String> getALL() {
        connect();

        try {
            Statement stmt = c.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM Chat ORDER BY id DESC LIMIT 5");
            while (r.next()) {
                story.add(r.getString("Message"));
                System.out.println(r.getString("Message"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return story;
    }

    void add(String Msg) {
        try {
            PreparedStatement pst = c.prepareStatement("insert into Chat (Message) VALUES (?)");
            pst.setString(1, Msg);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public myClient(Socket socket, int index) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.id = index + 1;
        count++;
        gson = new GsonBuilder().setPrettyPrinting().create();

        //story = new ArrayList<>();
        getALL();

        start();

    }

    public void run() {

        try {
            /*
            for (String msg : story) {
                this.Send(msg);
            }
            */
           for (int i = story.size() - 1; i >= 0; i--)
               this.Send(story.get(i));
            /*
            if (story.size() <= 5){
            for (String msg : story) {
                this.Send(msg);
            }
            } else {
                for(int i = 5; i > 0; i--){
                    this.Send(story.get(story.size()-i));
                }
                
            }
            */
            
            
            while (true) {
                String word = "";
                int ch;
                long i = 0;

                while ((ch = in.read()) >= 0) {
                    char c = (char) ch;

                    word += c;
                    if (c == '}') {
                        break;
                    }

                }
                System.out.print(word);
                add(word);

                for (myClient mc : Server.clients) {

                    if (mc != this) {
                        mc.Send(word);
                    }

                }

                from_to_me = gson.fromJson(word, Message.class);

                if (from_to_me.question && !voting) {
                    voting = true;
                    itsme = true;

                }

                if (from_to_me.question || voting) {
                    if (!itsme && !i_voted) {

                        if (from_to_me.message.equals("y")) {
                            yes++;
                            i_voted = true;
                        } else {
                            no++;
                            i_voted = true;
                        }
                    }

                    if (itsme) {

                        if (yes + no == count - 1) {

                            for (myClient mc : Server.clients) {
                                word = "Results Yes: " + yes + " No: " + no;
                                from_to_me.message = word;
                                word = gson.toJson(from_to_me);
                                mc.Send(word);

                            }

                            yes = 0;
                            no = 0;
                            voting = false;
                            itsme = false;
                            for (myClient mc : Server.clients) {
                                mc.i_voted = false;
                            }

                        }
                    }

                }

            }

        } catch (IOException e) {
        }
    }

    void Send(String word) {
        try {
            out.write(word);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    public void Close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

public class Server {

    ;
    private static ServerSocket server; // серверсокет
    public static LinkedList<myClient> clients = new LinkedList<>();

    public static void main(String[] args) {
        try {
            server = new ServerSocket(4004);

            System.out.println("Server: Hello, I am server");
            Integer index = 0;
            while (true) {

                Socket client = server.accept();

                try {

                    clients.add(new myClient(client, index++));

                    System.out.println("Client_" + clients.size() + ": I am here");

                } catch (IOException e) {
                    client.close();
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
