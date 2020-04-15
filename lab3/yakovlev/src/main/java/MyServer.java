
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */


public class MyServer {
    ServerSocket ss;
    int port;

    ArrayList<ThreadClient> clients = new ArrayList<>();

    int yes;
    int no;
    
    int clientsSize;
    
    boolean isVoting;
    
    public MyServer(int _port){
        port = _port;
        yes = 0;
        no = 0;
        isVoting = false;
    }
    
    public void sendAll(String message) throws IOException
    {
        for (int i = 0; i < clients.size(); i++)
        {
            clients.get(i).send(message);
        }
    }
    
    public boolean isAllVote()
    {
        if ((no+yes) == clientsSize)
            return true;
        else
            return false;
    }
    public void resetCounters()
    {
        no = 0;
        yes = 0;
    }
    
    public void serverStart() throws IOException, InterruptedException{
        System.out.println("Starting server\n");
        ss = new ServerSocket(port);
        System.out.println("Server started\n");
        
        Socket cs;
        int id = 0;
        
        while (true)
        {
             cs = ss.accept();
             id++;
             clientsSize++;
             clients.add(new ThreadClient(cs, id, this));
             System.out.println("Client #"+id+" connected\n");
        }

    }
    public static void main(String[] args) throws IOException, InterruptedException {
                new MyServer(2002).serverStart();

    }
}
