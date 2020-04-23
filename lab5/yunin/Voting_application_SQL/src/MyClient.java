import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient extends Thread {
    GUI gui;
    Gson gson;
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
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            while(true) {
                String s = ois.readUTF();
                Message msg = gson.fromJson(s, Message.class);
                String type = msg.getType();
                switch (type) {
                    case "SUGGEST":
                        can_suggest = false;
                        can_vote = true;
                        gui.set_result(msg.getValue());
                        break;
                    case "RESULT":
                        gui.set_result(msg.getValue());
                        can_suggest = true;
                        can_vote = false;
                        break;
                    case "HISTORY":
                        gui.show_history(msg.getValue());
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