package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Олеся
 */


public class ClientThread extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    Server sv;
    Socket cs;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public ClientThread(Socket cs, Server sv) {
        this.cs = cs;
        this.sv = sv;
        try {
            dos = new  DataOutputStream(cs.getOutputStream());
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
        try {
            dis = new DataInputStream(cs.getInputStream());
            while(true) {
                String s = dis.readUTF();
                Message m = gson.fromJson(s, Message.class);
                
                if(m.getType().equals("voting")) {
                    sv.bcast(s); 
                } else if (m.getType().equals("answer")) {
                    if(sv.vote(m.getMessage())) {
                       Message res = sv.votingRes();
                       sv.bcast(gson.toJson(res));
                    }
                }
            
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
