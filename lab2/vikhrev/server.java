/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ivanviikhrev
 */

import java.net.*;
import java.io.*;

public class Server {
    private void send(BufferedWriter writer, String m) throws IOException {
        writer.write(m);
        writer.newLine();
        writer.flush();          
    }
    
    private String receive(BufferedReader reader) throws IOException {
        String r;
        r = reader.readLine();
        return r;
    }
    
    
    public static void main(String[] args) throws IOException {
        Server s = new Server();
        System.out.println("Server started");
        ServerSocket serv = new ServerSocket(8000);
        Socket client1 = serv.accept();
        System.out.println("#1 connected");
        BufferedWriter writer1 = new BufferedWriter(
                    new OutputStreamWriter(client1.getOutputStream())); 
        BufferedReader reader1 = new BufferedReader(
                    new InputStreamReader(client1.getInputStream()));
         
        s.send(writer1, "1. Start chatting:" );
        
        Socket client2 = serv.accept();
        System.out.println("#2 connected");
        BufferedWriter writer2 = new BufferedWriter(
                    new OutputStreamWriter(client2.getOutputStream())); 
        BufferedReader reader2 = new BufferedReader(
                    new InputStreamReader(client2.getInputStream()));
        
        s.send(writer2, "2. Wait for #1:" );
        
        String response = "";
//        String answer = "";
     
        while(client1.isConnected() && client2.isConnected()){
            response = s.receive(reader1);

            s.send(writer2, "#1 " + response);
               
            response = s.receive(reader2);
            s.send(writer1, "#2 " + response);
        }
        System.out.println("Users disconnect");
        writer1.close();
        reader1.close();
        client1.close();
        writer2.close();
        reader2.close();
        client2.close();
        serv.close();
//        clientS.getOutputStream().write("Start session".getBytes());
        
//        servS.close();
        
        
    }
}
