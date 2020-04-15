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

public class JClient {
    private Socket clientSocket;
    private BufferedWriter bufWriter;
    private BufferedReader bufReader;
    private BufferedReader bufConsole;
    private boolean second = true;

    public JClient(int port) throws UnknownHostException, IOException {
        clientSocket = new Socket(InetAddress.getLocalHost(), port);
        bufWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        bufReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        bufConsole = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() throws IOException{
        String message = "";
        String answer = "";
        while(true) {
            try {
                answer = bufReader.readLine();
                if (answer != null && !answer.equals("*wait*")) {
                    second = false;
                }
                if (answer != null && answer.equals("#kill")) {
                    System.out.println("--- Other user disconnected ---");
                    break;
                }
                System.out.println(answer);
                if(!second){
                    message = bufConsole.readLine();
                    bufWriter.write(message);
                    bufWriter.newLine();
                    bufWriter.flush();
                    second = true;
                    if(message.equals("#kill")) {
                        System.out.println("--- You disconnected ---");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        bufWriter.close();
        bufReader.close();
        bufConsole.close();
        if (!clientSocket.isClosed()) {
            clientSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        JClient client = new JClient(1001);
        client.start();
    }
}
