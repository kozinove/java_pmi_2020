import java.io.*;
import java.net.*;

public class client {

    // socket and i/o streams
    private Socket client_socket;
    private BufferedWriter out;
    private BufferedReader in;
    private BufferedReader console;
    private boolean second = true;

    public client(int port) throws UnknownHostException, IOException {
        // create socket and i/o streams
        client_socket = new Socket(InetAddress.getLocalHost(), port);
        out = new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public void go() throws IOException{
        String message = "";
        String answer = "";
        while(true) {
            try {
                // take answer from server and check number of client (first or second)
                answer = in.readLine();
                if (answer != null && !answer.equals("*wait*")) {
                    second = false;
                }
                // check that second client don't stop the chat
                if (answer != null && answer.equals("#stop")) {
                    System.out.println("--- Other user disconnected ---");
                    break;
                }
                // show the message
                System.out.println(answer);
                // write answer message if its right queue
                if(!second){
                    message = console.readLine();
                    out.write(message);
                    out.newLine();
                    out.flush();
                    second = true;
                    // check that we stop the chat
                    if(message.equals("#stop")) {
                        System.out.println("--- You disconnected ---");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        // close the socket and i/o streams
        out.close();
        in.close();
        console.close();
        if (!client_socket.isClosed()) {
            client_socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        client cli = new client(5555);
        cli.go();
    }
}
