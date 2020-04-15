/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Олеся
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server_by_zolotareva {
    /**
     *
     * @param args
     * @throws InterruptedException
     */
    
    
    public static void main(String[] args) throws InterruptedException {
       
        
        try (ServerSocket server = new ServerSocket(4004)){
            Socket first = server.accept();
            Socket second = server.accept();
                       
            DataOutputStream f_out = new DataOutputStream(first.getOutputStream());
            DataOutputStream sec_out = new DataOutputStream(second.getOutputStream());
            DataInputStream f_in = new DataInputStream(first.getInputStream());
            DataInputStream sec_in = new DataInputStream(second.getInputStream());
                        
            boolean flag1 = true;
            boolean flag2 = false;

            f_out.writeUTF("write first.");
            f_out.flush();
            
            while(!first.isClosed() && !second.isClosed()){
                if(flag1) {
                    String enter_mess = f_in.readUTF();
                    System.out.println("First ask: " + enter_mess);
                    
                    sec_out.writeUTF(enter_mess);
                    if(enter_mess.equals(("q"))){
                        System.out.println("First user come out");
                        break;
                    }
                    flag1 = false;
                    flag2 = true;
                }
                if(flag2){
                    String enter_mess = sec_in.readUTF();
                    System.out.println("Second ask: "+ enter_mess);
                    f_out.writeUTF(enter_mess);
                    if(enter_mess.equals(("q"))){
                        System.out.println("Second user come out");
                        break;
                    }
                    flag1 = true;
                    flag2 = false;
                  
                }
            }

            f_in.close();
            f_out.close();

            sec_in.close();
            sec_out.close();

            first.close();
            second.close();
            
            System.out.println("Closing connections");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
