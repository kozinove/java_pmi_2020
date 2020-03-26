
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class MyClient {
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;
    int port;
    
    public MyClient(int _port){
        port = _port;
    }
    
    
    public void ClientStart() throws UnknownHostException, IOException{
        boolean IAmFirst = false;
        
        cs = new Socket(InetAddress.getLocalHost(),port);
        System.out.print("Client start \n");
        is = cs.getInputStream();
        os = cs.getOutputStream();  
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String firstMessage = dis.readUTF();
        System.out.println(firstMessage);
        if (firstMessage.contains("first")) IAmFirst = true;
        
        while(true)
        {
            String s = dis.readUTF();
            if (s.contains("STOP")) break;
            if (IAmFirst && s != null && s.charAt(0) == '2')
            {
                System.out.println(s);
                String line = reader.readLine();
                dos.writeUTF("1: " + line);
                dos.flush();
                if (line.contains("STOP")) break;
                System.out.println("wait...");
            }
            else if (IAmFirst == false && s != null && s.charAt(0) == '1')
            {
                System.out.println(s);
                String line = reader.readLine();
                dos.writeUTF("2: " + line);
                dos.flush();
                if (line.contains("STOP")) break;
                System.out.println("wait...");
            }
        }

        dis.close();
        dos.close();
        cs.close();
    }
    
    public static void main(String[] args) throws IOException {
        new MyClient(2001).ClientStart();
    }
}
