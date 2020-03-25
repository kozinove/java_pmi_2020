
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
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
public class MyServer {
    ServerSocket ss;
    Socket[] cs;
    DataInputStream[] dis;
    DataOutputStream[] dos;
    InputStream[] is;
    OutputStream[] os;
    int port;
    
    public MyServer(int _port, int numOfClients){
        cs = new Socket[numOfClients];
        dis = new DataInputStream[numOfClients];
        dos = new DataOutputStream[numOfClients];
        is = new InputStream[numOfClients];
        os = new OutputStream[numOfClients];
        port = _port;
    }
    
    public void serverStart() throws IOException, InterruptedException{
        System.out.println("Starting server\n");
        ss = new ServerSocket(port);
        System.out.println("Server started\n");
        cs[0] = ss.accept();
        is[0] = cs[0].getInputStream();
        os[0] = cs[0].getOutputStream();
        dis[0] = new DataInputStream(is[0]);
        dos[0] = new DataOutputStream(os[0]);
        dos[0].writeUTF("you are first");
        System.out.println("First client connected\n");
        cs[1] = ss.accept();
        is[1] = cs[1].getInputStream();
        os[1] = cs[1].getOutputStream();
        dis[1] = new DataInputStream(is[1]);
        dos[1] = new DataOutputStream(os[1]);
        dos[1].writeUTF("you are second");
        System.out.println("Second client connected\n");
        
        dos[0].writeUTF("2. start the chat");
        
        while(true)
        {
            String str1 = dis[0].readUTF();
            System.out.println(str1);
            dos[1].writeUTF(str1);
            if (str1.contains("STOP")) break;

            String str2 = dis[1].readUTF();
            System.out.println(str2);
            dos[0].writeUTF(str2);
            if (str2.contains("STOP")) break;
        }
        
        dos[0].close();
        dis[0].close();
        cs[0].close();
        dos[1].close();
        dis[1].close();
        cs[1].close();
        ss.close();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        new MyServer(2001,2).serverStart();
    }
}
