import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    DataBase DB;
    
    public ThreadClient(Socket _cs, int _id, MyServer _ser, DataBase _DB) throws IOException, SQLException{
        cs = _cs;
        id = _id;
        ser = _ser;
        is = cs.getInputStream();
        os = cs.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        System.out.println("id: "+ id);
        send(Integer.toString(id),id);
        // Database work
        DB = _DB;
        String lastMes = DB.getHistory();
        send(lastMes,-1);
        
        
        start();
    }
    
    public void send(String message, int id) throws IOException
    {
        Letter ltr = new Letter();
        ltr.setMes(message);
        ltr.id = id;
        dos.writeUTF(gson.toJson(ltr));
        dos.flush();
    }
    
    public Letter recv() throws IOException, SQLException
    {
        Letter ltr = new Letter();
        String message = dis.readUTF();
        ltr = gson.fromJson(message, Letter.class);
        System.out.println("Client" + ltr.id +" sent me: "+ ltr.getMes());
        if (ltr.getMes() != "") DB.UpdateHistory(ltr.getMes());
        return ltr;
    }
    
    public void run() {
        Letter recvBuf = new Letter();
        while(true)
        {
            try {
                recvBuf = recv();
                        } catch (IOException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (recvBuf.startVote())
            {
                ser.isVoting = true;
                try {
                    ser.sendAll("Start_vote", recvBuf.id);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ( ser.isVoting)
            {
                Letter answer = new Letter();
                while(true)
                {
                    try {
                        answer = recv();
                    } catch (IOException ex) {
                        Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (answer.answer == 1)
                    {
                        ser.yes++;
                        break;
                    }
                    else if (answer.answer == 0)
                    {
                        ser.no++;
                        break;
                    }
                    else
                    {
                        try {
                            ser.sendAll(answer.getMes(),answer.id);
                        } catch (IOException ex) {
                            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
            else
            {
                try {
                    ser.sendAll(recvBuf.getMes(),recvBuf.id);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ser.isAllVote())
            {
                try {
                    ser.sendAll("END OF VOITING\ncount YES = " + ser.yes + "\ncount NO = " + ser.no, -1);
                } catch (IOException ex) {
                    Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                ser.resetCounters();
                ser.isVoting = false;
            }
        }
    }
}
