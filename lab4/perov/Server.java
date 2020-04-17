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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class myClient extends Thread {

    private static Gson gson;

    int id;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    Message from_to_me;

    boolean i_voted = false;
    private static boolean voting = false;
    boolean itsme = false;
     
    private static int yes = 0;
    private static int no = 0;
    private static boolean flag = false;
    private static int count = 0;

    public myClient(Socket socket, int index) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.id = index + 1;
        count++;
        gson = new GsonBuilder().setPrettyPrinting().create();

        start();
    }

    public void run() {

        try {

            while (true) {
                String word = "";
                int ch;
                long i = 0;

                while ((ch = in.read()) >= 0) {
                    char c = (char) ch;

                    word += c;
                    if (c == '}') {
                        break;
                    }

                }
                System.out.print(word);

                for (myClient mc : Server.clients) {

                  if(mc != this)

                        mc.Send(word);

                    
                }
  
                  from_to_me = gson.fromJson(word, Message.class);
                  
                  
                  if(from_to_me.question && !voting) {
                      voting = true;
                       itsme = true;
                      
                  }
                  
                  
                  if(from_to_me.question || voting) {
                     if (  !itsme &&  !i_voted) {
                         
                        if (from_to_me.message.equals("y")) {
                            yes++;
                            i_voted = true;
                        } else {
                            no++;
                            i_voted = true;
                        }
                    }
                     
                     if (itsme) {

                        if (yes + no == count-1) {

                            for (myClient mc : Server.clients) {
                                word = "Results Yes: " + yes + " No: " + no;
                                from_to_me.message = word;
                                word = gson.toJson(from_to_me);
                                mc.Send(word);
                                
                            }

                            yes = 0;
                            no = 0;
                            voting = false;
                            itsme = false;
                            for (myClient mc : Server.clients) {
                                mc.i_voted = false;
                            }

                        }
                    }
                     
                      
                  }
         
                  
            }

        } catch (IOException e) {
        }
    }

    void Send(String word) {
        try {
            out.write(word);
            out.flush();
        } catch (IOException ignored) {
        }
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

}

public class Server {

    ;
    private static ServerSocket server; // серверсокет
    public static LinkedList<myClient> clients = new LinkedList<>();

    public static void main(String[] args) {
        try {
            server = new ServerSocket(4004);
            System.out.println("Server: Hello, I am server");
            Integer index = 0;
            while (true) {

                Socket client = server.accept();

                try {

                    clients.add(new myClient(client, index++));

                    System.out.println("Client_" + clients.size() + ": I am here");

                } catch (IOException e) {
                    client.close();
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
