package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Database.MyDatabase;
import Message.MyMessage;

public class MyServer {
    int port = 5555;
    InetAddress ip = null;
    ArrayList<ClientThread> all_clients = new ArrayList<ClientThread>();
    MyDatabase db;

    public void bcast(MyMessage message)
    {
        for (ClientThread client : all_clients) {
            client.send(message);
        }
    }

    /*public void ucast(MyMessage message)
    {
        for (ClientThread client : all_clients) {
            if (client.name == message.getDestination())
                client.send(message);
        }
    }*/

    public MyServer()
    {
        ServerSocket ss;
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server started\n");
            // create our database
            db = new MyDatabase();
            while(true)
            {
                cs = ss.accept();
                all_clients.add(new ClientThread(cs, this, db));
                System.out.append(Integer.toString(all_clients.size()) + " client connected ===\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.append("Server is starting\n");
        new MyServer();
    }
}