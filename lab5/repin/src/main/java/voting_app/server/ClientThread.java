/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting_app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import voting_app.message.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 *
 * @author mezotaken
 */
public class ClientThread extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    Gson gson;
    Server server;
    Socket client_socket;
    int id;
    
    public ClientThread(Socket cs, Server sv, int id_) {
        client_socket = cs;
        server = sv;
        id = id_;
        try {
            dos = new DataOutputStream(client_socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
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
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            dis = new DataInputStream(client_socket.getInputStream());
            while(true) {
                String s = dis.readUTF();
                Message msg = gson.fromJson(s,Message.class);
                switch (msg.Type()) {
                    case "vote":
                        server.Vote(msg.Text());
                        break;
                    case "message":  
                        Message new_msg = new Message(msg.Type(),"Клиент #" + Integer.toString(id) + ": " + msg.Text()); 
                        server.db.AddEvent(new_msg.Text());
                        s = gson.toJson(new_msg);
                        server.bcast(s);
                        break;
                    case "start": 
                        server.question = msg.Text();
                        server.bcast(s);
                        break;                 
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
