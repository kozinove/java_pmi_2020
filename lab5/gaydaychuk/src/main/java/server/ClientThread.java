package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    DataInputStream dis;
    DataOutputStream dos;
    MainServer ms;
    Socket cs;

    public ClientThread(Socket cs, MainServer ms)
    {
        this.cs = cs;
        this.ms = ms;
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
                ms.bcast(s, this);
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
