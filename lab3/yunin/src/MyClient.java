import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient extends Thread {
    GUI gui;
    Socket client_socket;
    DataOutputStream oos;
    DataInputStream ois;
    boolean can_suggest = true;
    boolean can_vote = false;

    public MyClient(GUI gui) {
        this.gui = gui;
        try {
            client_socket = new Socket("localhost", 3345);
            oos = new DataOutputStream(client_socket.getOutputStream());
            ois = new DataInputStream(client_socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        start();
    }

    public void send(String s) {
        try {
            oos.writeUTF(s);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while(true) {
                String s = ois.readUTF();
                String type = s.split("@")[0];
                switch (type) {
                    case "SUGGEST":
                        can_suggest = false;
                        can_vote = true;
                        gui.set_result(s.split("@")[1]);
                        break;
                    case "RESULT":
                        String res = s.split("@")[1];
                        gui.set_result(res);
                        can_suggest = true;
                        can_vote = false;
                        break;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}