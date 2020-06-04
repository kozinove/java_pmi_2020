package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Message.MyMessage;
import Database.MyDatabase;

public class ClientThread extends Thread{
    DataInputStream dis;
    DataOutputStream dos;
    MyServer sv;
    Socket cs;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MyDatabase db;
    String name = "Unnamed";

    public ClientThread(Socket cs, MyServer ss, MyDatabase db)
    {
        //this.name = "Unnamed";
        this.cs = cs;
        this.sv = ss;
        this.db = db;
        try {
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }


        String mssgs = this.db.getMessages();
        if(!mssgs.equals("")) {
            this.send(new MyMessage("history", mssgs));
        }
        String users = this.db.getUsers();
        if(!users.equals("")) {
            this.send(new MyMessage("users", users));
        }

        start();
    }

    public void send(MyMessage message)
    {
        try {
            dos.writeUTF(gson.toJson(message));
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
                MyMessage m = gson.fromJson(s, MyMessage.class);

                if (m.getType().equals("bcast")) {
                    db.add(m.getName(), m.getMessage());
                    sv.bcast(m);
                } else if (m.getType().equals("whisper")) {
                    //sv.ucast(m);
                    sv.bcast(m);
                } else if (m.getType().equals("add")) {
                    db.add(m.getName());
                    sv.bcast(new MyMessage("system", m.getName() + " connected to the channel"));
                    String users = this.db.getUsers();
                    if(!users.equals("")) {
                        sv.bcast(new MyMessage("users", users));
                    }
                } else if (m.getType().equals("remove")) {
                    db.delete(m.getName());
                    sv.bcast(new MyMessage("system", m.getName() + " left the channel"));
                    String users = this.db.getUsers();
                    if(!users.equals("")) {
                        sv.bcast(new MyMessage("users", users));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}