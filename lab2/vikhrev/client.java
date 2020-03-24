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
import java.util.Scanner;

public class Client {
    private Socket client;
    private BufferedWriter writer;
    private BufferedReader serverReader;
    private BufferedReader consoleReader;
    
    private String message;
    private String response;
    
    private boolean waiting = true;
    
    public Client() throws UnknownHostException, IOException {
        client = new Socket(InetAddress.getLocalHost(), 8000);
        writer = new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())); 
        serverReader = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
    }
    
    private String receive() throws IOException {
        String r;
        r = serverReader.readLine();
        if (r != null && r.charAt(0)!= '2') {
            waiting = false;            
        }
        return r;
    }
    
    private void send(String m) throws IOException{
        writer.write(m);
        writer.newLine();
        writer.flush();
        waiting = true;
     
    }
    public void run() throws IOException{
        while(true) {
            try {
                response = receive(); 
                System.out.println(response);
                if(!waiting){
                    message = consoleReader.readLine();
                    System.out.println("!exit".equals(message));
                    if("!exit".equals(message)){
                       System.out.println("Disconnect");
                       send("User disconnects. Press enter to exit.");
                       break;
                    }
                    send(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.close();
        consoleReader.close();
        serverReader.close();
        client.close();
    }
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client c = new Client();
	c.run();
    }
}

