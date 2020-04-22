package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

    public ClientThread(Socket cs, MyServer ss, MyDatabase db)
    {
        this.cs = cs;
        this.sv = ss;
        this.db = db;
        try {
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        // create the string with our 5 last messages and if not empty send it
        String mssgs = this.db.getMessages();
        if(!mssgs.equals("")) {
            this.send(gson.toJson(new MyMessage("history", mssgs)));
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
                MyMessage m = gson.fromJson(s, MyMessage.class);
                // if message is a votes, count the votes and bcast
                if (m.getType().equals("answer")) {
                    if (sv.vote(m.getMessage())) {
                        MyMessage res = sv.result_vote();
                        sv.bcast(gson.toJson(res));
                    }
                }
                // if message is a bcast message, add to database and bcast
                else if (m.getType().equals("bcast")){
                    db.add(m.getMessage());
                    sv.bcast(s);
                }
                // if message is a voting or history - bcast
                else {
                    sv.bcast(s);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}