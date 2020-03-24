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
    private ServerSocket server;
    private Socket client1;
    private Socket client2;
    private BufferedWriter writer1;
    private BufferedWriter writer2;
    private BufferedReader reader1;
    private BufferedReader reader2;
    
    public Server() throws UnknownHostException, IOException {
        System.out.println("Server started");
        server = new ServerSocket(8000);
        
    }
    private void send(BufferedWriter writer, String m) throws IOException {
            writer.write(m);
            writer.newLine();
            writer.flush();
    }
    
    private String receive(BufferedReader reader) throws IOException {
        String r = "";
        r = reader.readLine();
        return r;
    }
    
    public void run() throws IOException {
        client1 = server.accept();
        System.out.println("#1 connected");
        writer1 = new BufferedWriter(
                    new OutputStreamWriter(client1.getOutputStream())); 
        reader1 = new BufferedReader(
                    new InputStreamReader(client1.getInputStream()));         
        send(writer1, "1/. Start chatting:" );
        
        client2 = server.accept();
        System.out.println("#2 connected");
        writer2 = new BufferedWriter(
                    new OutputStreamWriter(client2.getOutputStream())); 
        reader2 = new BufferedReader(
                    new InputStreamReader(client2.getInputStream()));
        send(writer2, "2/. Wait for #1:" );
        
        String response = "";
     
        while(true){
            response = receive(reader1);
            send(writer2, "#1 " + response);
            if(response.equals("!exit")) {
                break;
            }
            
            response = receive(reader2);
            send(writer1, "#2 " + response); 
            if(response.equals("!exit")) {
                
                break;
            }
        }
        System.out.println("Users disconnected");
        writer1.close();
        reader1.close();
        if(!client1.isClosed()) {
            client1.close();    
        }
        writer2.close();
        reader2.close();
        if(!client2.isClosed()) {
            client2.close();  
        }
        server.close();
    }
    
    public static void main(String[] args) throws IOException {
        Server c = new Server();
	c.run();
    }
}
