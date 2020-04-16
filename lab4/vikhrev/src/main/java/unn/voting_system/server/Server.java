/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unn.voting_system.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import unn.voting_system.message.Message;
/**
 *
 * @author user
 */

public class Server {
    int port = 3124;
    InetAddress ip = null;
    ArrayList<ClientThread> allClients = new ArrayList<>();
    int countYes = 0;
    int countNo = 0;
    
    public void bcast(String s) {
        for(ClientThread client : allClients) {
            client.send(s);
        }
    } 
    
    synchronized public boolean vote(String s) {
        if(s.equals("yes")) 
            ++countYes;
        
        if(s.equals("no"))
            ++countNo;
        
        boolean res = (countYes + countNo) == allClients.size();
        
        return  res;
    }
    
    
    synchronized public Message votingRes(){ 
        Message m = new Message("result", "Voting result:\nYes - " +  Integer.toString(countYes) + '\n'  
                                          + "No - " + Integer.toString(countNo) + '\n' );
        countYes = countNo = 0;
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
                System.out.append("Client connected\n");
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
