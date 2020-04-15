package client;

import server.ClientThread;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServerThread extends Thread{
    MainFrame mf;

    int port = 3124;
    InetAddress ip = null;

    Socket cs;

    DataInputStream dis;
    DataOutputStream dos;

    public ClientServerThread(MainFrame mf) {
        this.mf = mf;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
        }
        try {
            cs = new Socket(ip, port);
            System.out.append("Client start \n");
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientServerThread.class.getName()).log(Level.SEVERE, null, ex);
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
                mf.addTextToFrame(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
