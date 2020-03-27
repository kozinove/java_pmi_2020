import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//message tags: 
//0 = Server info
//1 = ID
//2 = Data
//3 = Quit

public class Client {
    private int port = 8888;
    private InetAddress ip;
    private Socket client_socket;
    private BufferedWriter out;
    private BufferedReader in_server;
    private BufferedReader in_console;
    boolean active;
    private boolean alive = true;

    public void Write(String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }
    public String Read() throws IOException {
        return in_server.readLine();
    }

    public void Close() throws IOException {
        client_socket.close();
        out.close();
        in_server.close();
        in_console.close();
    }
    public void StartClient() {
        try {
            ip = InetAddress.getLocalHost();
            try {
                client_socket = new Socket(ip, port);
                out = new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream())); 
                in_server = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
                in_console = new BufferedReader(new InputStreamReader(System.in));
                while(alive) {
                    String msg = Read();
                    String tag = msg.substring(0,1);
                    msg = msg.substring(2);
                    switch (tag) {
                        case "0": 
                            System.out.println(msg); 
                            break;
                        case "1": 
                            if(msg.equals("0")) {
                                active = true;
                                System.out.println("You speak first.");
                            } else {
                                active = false;
                                System.out.println("Wait for another user to speak.");
                            }
                            break;
                        case "2":
                            System.out.println("Another user: " + msg);
                            active = true;
                            break;
                        case "3":
                            alive = false;
                            break;
                    }
                    if(active){ 
                        System.out.print("You:");     
                        msg = in_console.readLine();
                        Write(msg);
                        active = false;
                    }
                } 
                Close();
                System.out.println("Conversation ended.");
            }
            catch(IOException IOex) {
                System.out.println("I/O error occured.");
            }
        }
        catch(UnknownHostException UHEex) {
            System.out.println("Local host name cannot be resolved.");
        }
    }   
    public static void main(String[] args) {
        Client c = new Client();
	    c.StartClient();
    }
}
