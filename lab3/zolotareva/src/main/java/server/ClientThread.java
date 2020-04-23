package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Олеся
 */
public class ClientThread extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    Server sv;
    Socket cs;

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
                
                if(s.equals("yes") || s.equals("no")) {
                    boolean  res = sv.vote(s);
                    if(res) {
                        sv.bcast(sv.votingRes());
                    }
                } else {
                    sv.bcast(s);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
