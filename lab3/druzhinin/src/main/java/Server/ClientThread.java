package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    DataInputStream dis;
    DataOutputStream dos;
    MyServer sv;
    Socket cs;

    public ClientThread(Socket cs, MyServer ss)
    {
        this.cs = cs;
        this.sv = ss;
        try {
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    public void send(String s)
    {
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

            while(true)
            {
                String s = dis.readUTF();
                // if message is a votes, count the votes
                if(s.equals("vote:pro") || s.equals("vote:con")) {
                    boolean res = sv.vote(s);
                    // if vote end, send the result
                    if(res) {
                        sv.bcast(sv.result_vote());
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