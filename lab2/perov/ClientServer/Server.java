/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author perov
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

class myClient {

    int index;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;

    public myClient(Socket socket, int id) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        index = id+1;
    }

    public void Close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String Message() {
        String word = "";
        try {

            word = in.readLine();
            System.out.println("Client_" + index + ": " + word + "\n");

        } catch (IOException ex) {
            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return word;
    }

}

public class Server {

    private static LinkedList<myClient> clients = new LinkedList<>();
    ;
    private static ServerSocket server; // серверсокет

    public static void main(String[] args) {
        try {
            server = new ServerSocket(4004, 2);
            System.out.println("Server: Hello, I am server");
            int id = 0;
            while (clients.size() != 2) {

                Socket client = server.accept();
                try {
                    clients.add(new myClient(client, id++));
                     
            
                    System.out.println("Client_" + clients.size() +": I am here" );
                    
                } catch (IOException e) {
                    client.close();
                }

            }

            String word = "";
            boolean first = true;
            boolean second = true;
            
            clients.get(0).out.write("Start chat\n");
            clients.get(0).out.flush();  
           
            while (first && second) {

                if (first) {
                    word = clients.get(0).Message();
                    clients.get(1).out.write( word + "\n");
                    clients.get(1).out.flush(); 
                    if ("q".equals(word)) {
                        System.out.println("Client_1 - EXIT");
                        clients.get(0).Close();
                        first = false;

                    }

                }

                if (second) {
                    word = clients.get(1).Message();
                    clients.get(0).out.write( word + "\n");
                    clients.get(0).out.flush(); 
                    if ("q".equals(word)) {
                        System.out.println("Client_2 - EXIT");
                        clients.get(1).Close();
                        second = false;
                    }
                    
                    
                }
            }
            
          server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
