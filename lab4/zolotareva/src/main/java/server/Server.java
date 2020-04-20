package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
/**
 *
 * @author Олеся
 */

public class Server {
    int port = 3124;
    InetAddress ip = null;
    ArrayList<ClientThread> allClients = new ArrayList<>();
    int sayYes = 0;
    int sayNo = 0;
    
    public void bcast(String s) {
        for(ClientThread client : allClients) {
            client.send(s);
        }
    } 
    
    synchronized public boolean vote(String s) {
        if(s.equals("yes")) 
            ++sayYes;
        
        if(s.equals("no"))
            ++sayNo;
        
        boolean res = (sayYes + sayNo) == allClients.size();
        
        return  res;
    }
    
    
    synchronized public Message votingRes(){ 
        Message m = new Message("result", "Voting result:\nYes - " +  Integer.toString(sayYes) + '\n'  
                                          + "No - " + Integer.toString(sayNo) + '\n' );
        sayYes = sayNo = 0;
        return m;
    }
    
    synchronized public int clientsNum() {
        return allClients.size();
    }
    
    public Server() {
        ServerSocket ss;
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            ss = new ServerSocket(port, 0, ip);
            System.out.append("Server started\n");
            
            while(true){
                cs = ss.accept();
                allClients.add(new ClientThread(cs, this));
                System.out.append("Client connected...\n");
                System.out.append(Integer.toString(allClients.size()) + '\n');
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        System.out.append("Starting...\n");
        new Server();
    }
}
