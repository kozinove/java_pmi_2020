/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.mychat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.mychat.message.JMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author J-win
 */
public class ClientThread extends Thread {    
    DataInputStream dis;
    DataOutputStream dos;
    JServer server;
    Socket clientSocket;
    
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ClientThread(Socket clientSocket, JServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            dos = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public void run() {
        try {
            dis = new DataInputStream(clientSocket.getInputStream());

            while(true) {
                String message = dis.readUTF();
                JMessage messageJson = gson.fromJson(message, JMessage.class);
               
                if (messageJson.getType().equals("answer")) {
                    boolean res = server.vote(messageJson.getMessage());
                    if (res) {
                        JMessage m = new JMessage("end", "End voting: " + server.resultVote());
                        server.bcast(gson.toJson(m));
                    }
                } else {
                    server.bcast(message);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
