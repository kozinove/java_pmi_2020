import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    Gson gson;
    DataInputStream dis;
    DataOutputStream dos;
    MyServer server;
    Socket client_socket;

    public ClientThread(Socket cs, MyServer sv) {
        client_socket = cs;
        server = sv;
        try {
            dos = new DataOutputStream(client_socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        start();
    }

    public void send(String s) {
        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            dis = new DataInputStream(client_socket.getInputStream());
            while(true) {
                String s = dis.readUTF();
                Message msg = gson.fromJson(s, Message.class);
                String type = msg.getType();
                if(type.equals("SUGGEST")) {
                    server.bcast(s);
                } else {
                    server.vote(msg.getValue());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}