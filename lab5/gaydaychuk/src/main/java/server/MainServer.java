package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.Message;
import java.sql.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {
    int port = 3124;
    InetAddress ip = null;
    MessageManager pm = new MessageManager();
    Connection c;

    ArrayList<ClientThread> all_clients = new ArrayList<ClientThread>();

    void createDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:memory");
            System.out.println("In-memory DB created");
            Statement s = c.createStatement();

            s.executeUpdate("CREATE TABLE IF NOT EXISTS message_table (\n" +
                    "    id     INTEGER       PRIMARY KEY AUTOINCREMENT,\n" +
                    "    sender VARCHAR (20),\n" +
                    "    text   VARCHAR (200) \n" +
                    ")");



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void bcast(String s, ClientThread sender)
    {
        if(s.equals("default")){
            System.out.print("something is gone wrong");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Message m = new Message();
        try {
            m = mapper.readValue(s, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (m.getInitListRequest().equals("true")){
            String initList = pm.getPrevious();
            int id = all_clients.indexOf(sender);
            all_clients.get(id).send(initList);
            return;
        }
        if (m.getTargetMark().equals("true")){
            int id = Integer.parseInt(m.getTargetId());
            all_clients.get(id).send(m.getText());
            return;
        }
        String res = pm.getString(s, sender, all_clients);
        for (ClientThread all_client : all_clients) {
            if(!res.equals("")){
                all_client.send(res);
            }
        }
    }

    public MainServer()
    {

        ServerSocket ss;
        Socket cs;

        createDB();

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
        }

        try {
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server start\n");

            while(true)
            {
                cs = ss.accept();
                all_clients.add(new ClientThread(cs, this));
                System.out.append("Client connect \n");
            }


        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public static void main(String[] args) {
        new MainServer();
    }

}
