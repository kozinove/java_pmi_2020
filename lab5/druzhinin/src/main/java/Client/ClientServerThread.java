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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Message.MyMessage;

public class ClientServerThread extends Thread{
    Gui gui;
    boolean voting = false;
    boolean already_voted = false;
    int port = 5555;
    InetAddress ip = null;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
                    MyMessage m = gson.fromJson(s, MyMessage.class);
                    gui.UpdateText(m.getMessage());

                    // check, end vote or not
                    if (m.getType().equals("result")) {
                        end_vote();
                    }

                    // check, start vote or not
                    if (m.getType().equals("voting") && !voting) {
                        startVoting();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}