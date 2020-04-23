import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {
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
        try {
            dis = new DataInputStream(client_socket.getInputStream());
            while(true) {
                String s = dis.readUTF();
                String type = s.split("@")[0];
                if(type.equals("SUGGEST")) {
                    server.bcast(s);
                } else {
                    server.vote(s);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}