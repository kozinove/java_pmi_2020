import java.io.*;
import java.net.*;
import java.util.*;

public class OneByOneServer
{
    ArrayList<PrintWriter> clientOutputStreams = new ArrayList<PrintWriter>();
    int phase = 0;
    boolean stop = false;
    
    public class MessageReceiver{
        BufferedReader reader1;
        BufferedReader reader2;
        Socket sock1;
        Socket sock2;
        
        public MessageReceiver(Socket clientSOcket, Socket clientSOcket2) {
            try {
                sock1 = clientSOcket;
                sock2 = clientSOcket2;
                InputStreamReader isReader1 = new InputStreamReader(sock1.getInputStream());
                InputStreamReader isReader2 = new InputStreamReader(sock2.getInputStream());
                reader1 = new BufferedReader(isReader1);
                reader2 = new BufferedReader(isReader2);
                
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        
        public void doIt() {
            String message;
            try {
                while(!stop){
                    if(phase == 0){
                        while ((message = reader1.readLine()) == null) {
                            continue;
                        }
                        if(message.equals("stop")){
                            stopMessage();
                        }
                        printLastMessage("\t\t" + message);
                        phase = 1;
                    }
                    else{
                        while ((message = reader2.readLine()) == null) {
                            continue;
                        }
                        if(message.equals("stop")){
                            stopMessage();
                        }
                        printLastMessage("\t\t" + message);
                        phase = 0;
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    
    public static void main(String[] args) {
        new OneByOneServer().go();
    }
    
    public void go() {
        try {
            ServerSocket serverSock = new ServerSocket(5000);

            Socket clientSocket = serverSock.accept();
            PrintWriter writer1 = new PrintWriter(clientSocket.getOutputStream());
            clientOutputStreams.add(writer1);
            
            Socket clientSocket2 = serverSock.accept();
            PrintWriter writer2 = new PrintWriter(clientSocket2.getOutputStream());
            clientOutputStreams.add(writer2);

            writer1.println("first");
            writer1.flush();
            writer2.println("second");
            writer2.flush();
            
            MessageReceiver mr = new MessageReceiver(clientSocket, clientSocket2);
            mr.doIt();
            serverSock.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public void printLastMessage(String message) {
        PrintWriter writer = (PrintWriter)(clientOutputStreams.get((phase == 0 ? 1 : 0)));
        writer.println(message);
        writer.flush();
    }
    private void stopMessage(){
        stop = true;
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println("dialog stopped");
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
