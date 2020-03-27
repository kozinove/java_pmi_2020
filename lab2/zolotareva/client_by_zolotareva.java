/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Олеся
 */

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

// import java.io.DataInputStream;
// import java.io.DataOutputStream;/
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class client_by_zolotareva {

    
    public static void main(String[] args) throws InterruptedException {
        
        
        try (Socket client = new Socket("localhost", 4004);
        BufferedReader server = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        DataInputStream in = new DataInputStream(client.getInputStream());){
          
            while (true){
                
                String question = "";
                String answer = "";
                answer = in.readUTF();
                
                if (answer != null && answer.equals("#stop")) {
                    System.out.println("--- Other user disconnected ---");
                    break;
                }
                
                if(!"q".equals(answer)){
                System.out.println(answer);
                System.out.println("Write here: ");
                question = server.readLine();
                
                out.writeUTF(question + "\n");
                if (question != null && question.equals("#stop")) {
                    System.out.println("--- It's user disconnected ---");
                    break;
                }
                
                out.flush();
                
                client.close();
                in.close();
                out.close();
                }  
            }         
       
        
        } catch (IOException exs) {
            exs.printStackTrace();
        }    
    }     
}

