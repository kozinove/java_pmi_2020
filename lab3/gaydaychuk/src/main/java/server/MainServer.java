package server;

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
    PoleManager pm = new PoleManager();

    ArrayList<ClientThread> all_clients = new ArrayList<ClientThread>();

    public void bcast(String s, ClientThread sender)
    {
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
