/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unn.voting_system.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import unn.voting_system.server.ClientThread;
import unn.voting_system.server.Server;

import unn.voting_system.message.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author user
 */
public class ClientServerThread extends Thread{
    MainFrame mf;
    boolean voting = false;
    boolean voted = false;
    
    int port = 3124;
    InetAddress ip = null;
    DataInputStream dis;
    DataOutputStream dos;
    Socket cs;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ClientServerThread(MainFrame mf) {
        this.mf = mf;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            cs = new Socket(ip, port);
            System.out.append("Client started \n");
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        start();
    }
    
    public void startVoting() {
        voting = true;
        System.out.append("Voting has started\n");
    }
    
    public void endVoting() {
        voting = false;
        System.out.append("Voting has ended\n");
    }
    
    public boolean isVoting() {
        return voting;
    }
    
    public void canVote() {
        voted = false;
    }
    
    public void hasVoted() {
        voted = true;
    }
    
    public boolean isVoted() {
        return voted;
    }
    

    public void send(String s) {
        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    @Override
    public void run() {
        try {
            dis = new DataInputStream(cs.getInputStream());
            while(true) {
               String s = dis.readUTF();
         
               if(s != null){
                   Message m = gson.fromJson(s, Message.class);
                   
                   if(m.getType().equals("result")) {
                       endVoting();
                       canVote();
                   }
        
                   if(m.getType().equals("voting") && !isVoting()){
                       startVoting();
                   }
                   
                   mf.addMessage(m.getMessage());
               }
               
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    
    
}
