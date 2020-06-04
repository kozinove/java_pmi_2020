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
        String add = gson.toJson(new MyMessage("add", gui.name, ""));
        send(add);
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
            while(true) {
                String s = dis.readUTF();
                if(s != null) {
                    MyMessage m = gson.fromJson(s, MyMessage.class);
                    if (m.getType().equals("history")) {
                        gui.UpdateText(m.getMessage());
                    } else if (m.getType().equals("users")) {
                        gui.UpdateUsers(m.getMessage());
                    } else if (m.getType().equals("whisper")) {
                        if (m.getDestination().equals(gui.name)) {
                            gui.UpdateText("[" + m.getName() + "] -> [" + m.getDestination() + "]: " + m.getMessage());
                        }
                    } else if (m.getType().equals("system")){
                        gui.UpdateText(m.getMessage());
                    } else {
                        gui.UpdateText("[" + m.getName() + "]: " + m.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}