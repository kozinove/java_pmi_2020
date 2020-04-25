/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.mychat.client;

import chat.mychat.message.JMessage;
import chat.mychat.server.ClientThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J-win
 */
public class ClientServerThread extends Thread{
    ChatGUI gui;
    
    int port = 1001;
    InetAddress ip = null;
    
    Socket clientSocket;
    
    DataInputStream dis;
    DataOutputStream dos;
    
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public ClientServerThread(ChatGUI gui) {
        this.gui = gui;
        
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            clientSocket = new Socket(ip, port);
            System.out.append("=== Client started ===\n");
            dos = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gui.addTextToFrame("=== Сonect installed ===\n");
        
        start();
    }
    
    public void send(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void voting(String message) {
        try {
            dos.writeUTF("!voting! " + message);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(clientSocket.getInputStream());

            while(true) {
                String message = dis.readUTF();
                if (message != null) {
                    JMessage messageJson = gson.fromJson(message, JMessage.class);
                    
                    gui.addTextToFrame(messageJson.getMessage());
                    
                    if (messageJson.getType().equals("voting")) {
                        gui.blockBcast = false;
                        gui.blockVoting = false;
                        gui.blockVote = true;
                    }
                
                    if (messageJson.getType().equals("end")) {
                        gui.blockBcast = true;
                        gui.blockVoting = true;
                        gui.blockVote = false;
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
