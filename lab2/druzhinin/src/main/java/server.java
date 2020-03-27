import java.io.*;
import java.net.*;

public class server {

    // sockets and i/o streams
    private ServerSocket server_socket;
    private Socket client_socket1;
    private BufferedReader in1;
    private BufferedWriter out1;
    private Socket client_socket2;
    private BufferedReader in2;
    private BufferedWriter out2;

    public server(int port) throws UnknownHostException, IOException {
        // create server socket
        server_socket = new ServerSocket(port);
        System.out.println("--- Server started ---");
    }

    public void go() throws IOException {
        // wait first client
        client_socket1 = server_socket.accept();
        System.out.println("--- First client connected ---");
        out1 = new BufferedWriter(new OutputStreamWriter(client_socket1.getOutputStream()));
        in1 = new BufferedReader(new InputStreamReader(client_socket1.getInputStream()));
        // send special message to first client
        out1.write("*write*");
        out1.newLine();
        out1.flush();

        // wait second client
        client_socket2 = server_socket.accept();
        System.out.println("--- Second client connected ---");
        out2 = new BufferedWriter(new OutputStreamWriter(client_socket2.getOutputStream()));
        in2 = new BufferedReader(new InputStreamReader(client_socket2.getInputStream()));
        // send special message to second client
        out2.write("*wait*");
        out2.newLine();
        out2.flush();

        // ping-pong. first send message to second, then second send to first
        String text = "";
        while (true) {
            // take the message from first
            text = in1.readLine();
            // send the message to second
            out2.write(text);
            out2.newLine();
            out2.flush();
            // check exit command
            if (text.equals("#stop")) {
                System.out.println("--- First client disconnected ---");
                break;
            }
            // take the message from second
            text = in2.readLine();
            // send the message to first
            out1.write(text);
            out1.newLine();
            out1.flush();
            // check exit command
            if (text.equals("#stop")) {
                System.out.println("--- Second client disconnected ---");
                break;
            }
        }
        // if one disconnect, disconnect others and close the sockets
        System.out.println("--- All clients disconnected ---");
        out1.close();
        in1.close();
        if (!client_socket1.isClosed()) {
            client_socket1.close();
        }
        out2.close();
        in2.close();
        if (!client_socket2.isClosed()) {
            client_socket2.close();
        }
        server_socket.close();
    }

    public static void main(String[] args) throws IOException {
        server serv = new server(5555);
        serv.go();
    }
}
