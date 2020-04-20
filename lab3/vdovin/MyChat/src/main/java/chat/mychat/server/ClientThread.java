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

/**
 *
 * @author J-win
 */
public class ClientThread extends Thread {    
    DataInputStream dis;
    DataOutputStream dos;
    JServer server;
    Socket clientSocket;

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
               
                if (message.equals("!yes!") || message.equals("!no!") || message.equals("!abstain!")) {
                    boolean res = server.vote(message);
                    if (res) {
                        server.bcast(server.resultVote());
                        server.bcast("!end!");
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
