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

class myClient extends Thread {

    int index;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    boolean itsme = false;
    boolean vote = false;
    private static int yes = 0;
    private static int no = 0;
    private static boolean flag = false;
    private static int count = 0;

    public myClient(Socket socket, int id) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        index = id + 1;
        count++;
        start();
    }

    public void run() {
        String word;
        try {

            while (true) {
                word = in.readLine();
                if (word.equals("q")) {
                    break;
                }
                if (word.contains("?") && flag) {
                  word =   word.replaceAll("\\?", "");
                }
                for (myClient mc : Server.clients) {
                    if (mc != this) {

                        mc.Send(word);
                    }
                }

                if (word.contains("?") && !flag) {
                    itsme = true;
                    flag = true;
                }

                if (word.contains("?") || flag) {
                    if (!itsme && !vote) {

                        if (word.equals("y")) {
                            yes++;
                            vote = true;
                        } else {
                            no++;
                            vote = true;
                        }
                    }

                    if (itsme) {

                        if (yes + no == count - 1) {

                            for (myClient mc : Server.clients) {
                                mc.Send("#Results Yes: " + yes + " No: " + no);
                            }

                            yes = 0;
                            no = 0;
                            flag = false;
                            itsme = false;
                            for (myClient mc : Server.clients) {
                                mc.vote = false;
                            }

                        }
                    }
                }
            }

        } catch (IOException e) {
        }
    }

    private void Send(String word) {
        try {
            out.write(word + "\n");
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
            int id = 0;
            while (true) {

                Socket client = server.accept();
                try {
                    clients.add(new myClient(client, id++));

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
