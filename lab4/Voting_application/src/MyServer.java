import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MyServer {
    ArrayList<ClientThread> clients = new ArrayList<>();
    int yes_counter = 0;
    int no_counter = 0;

    public void bcast(String s) {
        for(ClientThread client : clients) {
            client.send(s);
        }
    }

    public void end_vote(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String res = gson.toJson(new Message("RESULT","+ : " + Integer.toString(yes_counter)
                + "\n - : " + Integer.toString(no_counter)+"\n"));
        yes_counter = no_counter = 0;
        bcast(res);
    }
    synchronized public void vote(String s) {
        if(s.equals("+"))
            ++yes_counter;
        else
            ++no_counter;
        if(yes_counter + no_counter == clients.size())
            end_vote();
    }
    public void start_server() {
        try (ServerSocket server=new ServerSocket(3345)){
            while(true){
                Socket  cs = server.accept();
                clients.add(new ClientThread(cs, this));
                System.out.append("Client #" + Integer.toString(clients.size()) + " connected.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        MyServer s = new MyServer();
        s.start_server();
    }
}