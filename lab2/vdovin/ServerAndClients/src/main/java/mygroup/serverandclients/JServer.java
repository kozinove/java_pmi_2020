/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygroup.serverandclients;

/**
 *
 * @author J-win
 */

import java.io.*;
import java.net.*;

public class JServer {
    private ServerSocket serverSocket;
    private Socket clientSocket1;
    private BufferedReader bufReader1;
    private BufferedWriter bufWriter1;
    private Socket clientSocket2;
    private BufferedReader bufReader2;
    private BufferedWriter bufWriter2;

    public JServer(int port) throws UnknownHostException, IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("--- Server started ---");
    }

    public void start() throws IOException {
        clientSocket1 = serverSocket.accept();
        System.out.println("--- First client connected ---");
        bufWriter1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));
        bufReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
        bufWriter1.write("*write*");
        bufWriter1.newLine();
        bufWriter1.flush();
        
        clientSocket2 = serverSocket.accept();
        System.out.println("--- Second client connected ---");
        bufWriter2 = new BufferedWriter(new OutputStreamWriter(clientSocket2.getOutputStream()));
        bufReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
        bufWriter2.write("*wait*");
        bufWriter2.newLine();
        bufWriter2.flush();

        String text = "";
        while (true) {
            text = bufReader1.readLine();
            bufWriter2.write(text);
            bufWriter2.newLine();
            bufWriter2.flush();
            if (text.equals("#kill")) {
                System.out.println("--- First client disconnected ---");
                break;
            }
            
            text = bufReader2.readLine();
            bufWriter1.write(text);
            bufWriter1.newLine();
            bufWriter1.flush();
            if (text.equals("#kill")) {
                System.out.println("--- Second client disconnected ---");
                break;
            }
        }
        
        System.out.println("--- All clients disconnected ---");
        
        bufWriter1.close();
        bufReader1.close();
        if (!clientSocket1.isClosed()) {
            clientSocket1.close();
        }
        
        bufWriter2.close();
        bufReader2.close();
        if (!clientSocket2.isClosed()) {
            clientSocket2.close();
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        JServer server = new JServer(1001);
        server.start();
    }
}
