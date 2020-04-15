package Client;

import Server.ClientThread;
import Server.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServerThread extends Thread{
    Gui gui;

    boolean voting = false;
    boolean already_voted = false;

    int port = 5555;
    InetAddress ip = null;

    Socket cs;

    DataInputStream dis;
    DataOutputStream dos;

    public ClientServerThread(Gui gui) {
        this.gui = gui;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            cs = new Socket(ip, port);
            System.out.append("=== Client start ===\n");
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

    public void startVoting() {
        voting = true;
        System.out.append("=== Voting has started ===\n");
    }

    // vote can 1 time
    public void imVoted() {
        already_voted = true;
    }

    // end vote, refresh variables
    public void end_vote() {
        voting = false;
        already_voted = false;
    }



    @Override
    public void run() {
        try {
            dis = new DataInputStream(cs.getInputStream());

            while(true) {
                String s = dis.readUTF();

                if(s != null) {
                    gui.UpdateText(s);

                    // check, end vote or not
                    if (s.length() > 7 && s.substring(0, 7).equals("!Voting")) {
                        end_vote();
                    }

                    // check, start vote or not
                    if (s.length() > 7 && s.substring(0, 7).equals("voting:") && !voting) {
                        startVoting();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}