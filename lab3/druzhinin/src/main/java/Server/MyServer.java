package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {
    int port = 5555;
    InetAddress ip = null;
    ArrayList<ClientThread> all_clients = new ArrayList<ClientThread>();
    int count_pro = 0;
    int count_con = 0;

    public void bcast(String message)
    {
        for (ClientThread client : all_clients) {
            client.send(message);
        }
    }

    // count votes and check is everyone voted
    synchronized public boolean vote(String s) {
        boolean is_finish;
        if(s.equals("vote:pro"))
            count_pro++;

        if(s.equals("vote:con"))
            count_con++;

        return (count_pro + count_con) == all_clients.size();
    }

    // when everyone is voted, write the result of vote
    synchronized public String result_vote() {
        String res = "!Voting is finished. Pro: " + Integer.toString(count_pro) + ", Con: " + Integer.toString(count_con) + "\n";
        count_pro = 0;
        count_con = 0;
        return res;
    }

    public MyServer()
    {
        ServerSocket ss;
        Socket cs;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ss = new ServerSocket(port, 0, ip);
            System.out.append("=== Server started ===\n");

            while(true)
            {
                cs = ss.accept();
                all_clients.add(new ClientThread(cs, this));
                System.out.append("=== " + Integer.toString(all_clients.size()) + " client connected ===\n");
            }


        } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public static void main(String[] args) {
        System.out.append("=== Server is starting ===\n");
        new MyServer();
    }

}