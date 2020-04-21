/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unn.voting_system.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

import unn.voting_system.message.Message;
import unn.voting_system.data_base.Data_base;

/**
 *
 * @author user
 */

public class ClientThread extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    Server sv;
    Socket cs;
    Data_base db;
    int id;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String last_messages = "";
    
    public ClientThread(Socket cs, Server sv, Data_base db, int i) {
        this.cs = cs;
        this.sv = sv;
        this.db = db;
        this.id = i;
        
        try {
            dos = new  DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList<String> temp = this.db.getMessages();
        
        if(!temp.isEmpty()) {
            for (int j = temp.size() - 1; j >= 0; j--) {
               last_messages += temp.get(j) + '\n';
                      
            }
            this.send(gson.toJson(new Message("last messages", "Last messages: \n" + last_messages + "--------------------- \n")));
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
            dis = new DataInputStream(cs.getInputStream());
            while(true) {
                String s = dis.readUTF();
                Message m = gson.fromJson(s, Message.class);
        
                if(m.getType().equals("voting") || m.getType().equals("bcast")) {
                    m.setID(id);
                    db.add(m.getMessage());
                    sv.bcast(gson.toJson(m)); 
                } else if (m.getType().equals("answer")) {
                    if(sv.vote(m.getMessage())) {
                       Message res = sv.votingRes();
                       db.add(res.getMessage());
                       sv.bcast(gson.toJson(res));
                    }
                }
            
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
