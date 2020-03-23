import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class OneByOneClient
{
    boolean isFirst = false;
    BufferedReader reader;
    BufferedReader consoleReader;

    PrintWriter writer;
    Socket sock;
    
    public void go() {
        connect();
        String message;
        String input;
        try {
            while(true){
                if(isFirst){
                    input = consoleReader.readLine();
                    writer.println(input);
                    writer.flush();
                    isFirst = false;
                }
                else{
                    while (
                        (message = reader.readLine()) != null) {
                        System.out.println(message);
                        isFirst = true;
                        break;
                    }
                }
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void connect() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("waiting for connection...\n");
            String initStatus = reader.readLine();
            if(initStatus.equals("first")){
                isFirst = true;
            }
            System.out.println("networking established,\n you " + (isFirst ? "can write smth" : "must wait your peer"));
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new OneByOneClient().go();
    }
}

