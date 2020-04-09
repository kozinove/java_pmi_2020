/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting_app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import voting_app.server.ClientThread;
import voting_app.server.Server;

/**
 *
 * @author mezotaken
 */
public class ClientServerThread extends Thread{
    MainFrame mf;
    int port = 8888;
    InetAddress ip = null;
    DataInputStream dis;
    DataOutputStream dos;
    Socket client_socket;
    boolean can_start = true;
    boolean can_vote = false;
    
    public ClientServerThread(MainFrame mf) {
        this.mf = mf;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            client_socket = new Socket(ip, port);
            dis = new DataInputStream(client_socket.getInputStream());
            dos = new DataOutputStream(client_socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        start();
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
            while(true) {
               String s = dis.readUTF();
               String tag = s.substring(0,1);
               String msg = s.substring(2);
               mf.AddTextToFrame(msg);
                switch (tag) {
                    case "0":  
                        can_start = false;
                        can_vote = true;
                        break;
                    case "1": 
                        can_start = true;
                        can_vote = false;
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    
    
}
