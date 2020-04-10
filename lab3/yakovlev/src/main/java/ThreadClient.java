
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class ThreadClient extends Thread {
    Socket cs;
    int id;
    
    DataInputStream dis;
    DataOutputStream dos;
    InputStream is;
    OutputStream os;
    MyServer ser;
    
    public ThreadClient(Socket _cs, int _id, MyServer _ser) throws IOException{
        cs = _cs;
        id = _id;
        ser = _ser;
        is = cs.getInputStream();
        os = cs.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        start();
    }
    
    public void send(String message) throws IOException
    {
        dos.writeUTF(message);
        dos.flush();
    }
    
    public String recv() throws IOException
    {
        String message = dis.readUTF();
        System.out.println("Client sent me: "+ message);
        return message;
    }
    
    public void run() {
        String recvBuf = "";
        while(true)
        {
            try {
                recvBuf = recv();
                        } catch (IOException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (recvBuf.contains("Start_vote"))
            {
                ser.isVoting = true;
                try {
                    ser.sendAll("Start_vote");
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ( ser.isVoting)
            {
                String answer = "";

                
                while(true)
                {
                    try {
                        answer = recv();
                    } catch (IOException ex) {
                        Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (answer.contains("yes"))
                    {
                        ser.yes++;
                        break;
                    }
                    else if (answer.contains("no"))
                    {
                        ser.no++;
                        break;
                    }
                    else
                    {
                        try {
                            ser.sendAll(answer);
                        } catch (IOException ex) {
                            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
            else
            {
                try {
                    ser.sendAll(recvBuf);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ser.isAllVote())
            {
                try {
                    ser.sendAll("END OF VOITING\ncount YES = " + ser.yes + "\ncount NO = " + ser.no);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                ser.resetCounters();
                ser.isVoting = false;
            }
        }
    }
}
