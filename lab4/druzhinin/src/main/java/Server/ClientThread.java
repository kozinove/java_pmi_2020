package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Message.MyMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientThread extends Thread{

    DataInputStream dis;
    DataOutputStream dos;
    MyServer sv;
    Socket cs;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
                MyMessage m = gson.fromJson(s, MyMessage.class);
                // if message is a votes, count the votes
                if (m.getType().equals("answer")) {
                    if (sv.vote(m.getMessage())) {
                        MyMessage res = sv.result_vote();
                        sv.bcast(gson.toJson(res));
                    }
                }
                else {
                    sv.bcast(s);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}