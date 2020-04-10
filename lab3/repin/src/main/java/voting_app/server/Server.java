/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting_app.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mezotaken
 */
//Теги сообщений: 
// 0 - начало голосования
// 1 - конец голосования

public class Server {
    private int port = 8888;
    private InetAddress ip;
    private ServerSocket server_socket;
    ArrayList<ClientThread> clients = new ArrayList<>();
    int count_no = 0;
    int count_yes = 0;
    boolean voting_now = false;
    
    public void bcast(String s) {
        for(ClientThread client : clients) {
            client.send(s);
        }
    } 
    
    synchronized public void Vote(String s) {
        if(s.equals("yes")) 
            count_yes++;   
        else
            count_no++;
        if(count_yes + count_no == clients.size()) 
            EndVote();
    }
    
    public void EndVote(){ 
        String res = "Результат голосования:\nДа: " + Integer.toString(count_yes) 
                     + "\nНет: " + Integer.toString(count_no)+"\n";
        count_no  = count_yes = 0;
        voting_now = false;
        bcast("1:" + res);
    }
    
    public void StartServer() { 
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            server_socket = new ServerSocket(port, 0, ip);
            System.out.append("Server started.\n");
            
            while(true){
                cs = server_socket.accept();
                clients.add(new ClientThread(cs, this));
                System.out.append("Client #" + Integer.toString(clients.size()) + " connected.\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Server s = new Server();
        s.StartServer();
    }
}
