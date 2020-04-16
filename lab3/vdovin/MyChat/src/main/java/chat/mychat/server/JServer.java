/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.mychat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J-win
 */
public class JServer {
    int port = 1001;
    InetAddress ip = null;
    
    ArrayList<ClientThread> allClients = new ArrayList<>();
    
    int voteYes = 0;
    int voteNo = 0;
    
    public void bcast (String message) {
        for (ClientThread client : allClients) {
            client.send(message);
        }
    }
    
    synchronized public boolean vote(String s) {
        if(s.equals("!yes!"))
            voteYes++;

        if(s.equals("!no!"))
            voteNo++;

        return (voteYes + voteNo) == allClients.size();
    }

    synchronized public String resultVote() {
        String res = "Yes - " + Integer.toString(voteYes) + "; no - " + Integer.toString(voteNo);
        voteYes = 0;
        voteNo = 0;
        return res;
    }
    
    public JServer() {
        ServerSocket serverSocket;
        Socket clientSocket;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            serverSocket = new ServerSocket(port, 0, ip);
            System.out.append("=== Server started ===\n");

            while(true) {
                clientSocket = serverSocket.accept();
                allClients.add(new ClientThread(clientSocket, this));
                System.out.append("=== " + Integer.toString(allClients.size()) + " client connected ===\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        System.out.append("=== Server is starting ===\n");
        new JServer();
    }
}
