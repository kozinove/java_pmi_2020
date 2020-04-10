
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
    boolean isVoting;
    
    public MyClient(int _port){
        port = _port;
    }
    
    public boolean isVoting()
    {
        return isVoting;
    }
    
    public void ClientStart() throws UnknownHostException, IOException{
        cs = new Socket(InetAddress.getLocalHost(),port);
        System.out.print("Client start \n");
        is = cs.getInputStream();
        os = cs.getOutputStream();  
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        
        new Thread(() -> { 
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = "";
            while (true)
            {
                try {
                    message = dis.readUTF();
                } catch (IOException ex) {
                    Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(message);
                if (message.contains("Start_vote"))
                {
                    try {
                        dos.writeUTF("");
                        dos.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isVoting = true;
                    System.out.println("Please, answer 'yes' or 'no':");
                }
            }
            
        }).start();
        
        
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            String line = "";

            line = reader.readLine();
            if (isVoting)
            {
                while(isVoting)
                {
                    String line2 = "";
                    if ( line.contains("yes") || line.contains("no"))
                    {
                        try {
                            dos.writeUTF(line);
                            dos.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        isVoting = false;
                    }
                    else
                    {
                        dos.writeUTF(line);
                        dos.flush();
                        line = reader.readLine();
                    }
                }
            }
            else
            {
                dos.writeUTF(line);
                dos.flush();
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        new MyClient(2002).ClientStart();
    }
}