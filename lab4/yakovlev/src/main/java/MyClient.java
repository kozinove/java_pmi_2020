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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    int id;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public MyClient(int _port){
        port = _port;
        id = -1;
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
        
        String idMessage = dis.readUTF();
        idMessage = gson.fromJson(idMessage, Letter.class).getMes();
        System.out.println("id: " + idMessage);
        id = Integer.parseInt(idMessage);
        
        new Thread(() -> { 
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = "";
            Letter ltr = new Letter();
            while (true)
            {
                
                try {
                    message = dis.readUTF();
                    ltr = gson.fromJson(message, Letter.class);
                } catch (IOException ex) {
                    Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ltr.id != id) System.out.println(ltr.getMes());
                if (ltr.startVote())
                {
                    try {
                        dos.writeUTF(gson.toJson(new Letter("",id)));
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
            Letter ltr = new Letter();
            ltr.id = id;

            line = reader.readLine();
            if (isVoting)
            {
                while(isVoting)
                {
                    ltr.setMes(line);
                    if ( ltr.answer > -1)
                    {
                        dos.writeUTF(gson.toJson(ltr));
                        dos.flush();
                        isVoting = false;
                    }
                    else
                    {
                        dos.writeUTF(gson.toJson(ltr));
                        dos.flush();
                        line = reader.readLine();
                    }
                }
            }
            else
            {
                ltr.setMes(line);
                dos.writeUTF(gson.toJson(ltr));
                dos.flush();
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        new MyClient(2002).ClientStart();
    }
}
