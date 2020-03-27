import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//Теги сообщений: 
//0 = Server info
//1 = ID
//2 = Data
//3 = Quit

public class Server {

    class Client {
        private Socket client_socket;
        private BufferedWriter out;
        private BufferedReader in;

        public Client(Socket socket) throws IOException {
            client_socket = socket;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        public void Write(String msg) throws IOException {
             out.write(msg);
             out.newLine();
             out.flush();
        }
        public String Read() throws IOException {
            return in.readLine();
        }
        public void Close() throws IOException {
            client_socket.close();
            out.close();
            in.close();
        }
    }

    private int port = 8888;
    private InetAddress ip;
    private ServerSocket server_socket;
    private Client[] clients = new Client[2];
    private int turn = 0;  // Чья очередь писать
    private boolean alive; // Статус текущей беседы

    public void StartServer() {
        try {
            ip = InetAddress.getLocalHost();
            try {
                server_socket = new ServerSocket(port, 0, ip);
                System.out.println("Server started.");
                while(true) {                                       // Цикл для сервера
                                                                    // Ожидание пользователей и отправка сведений
                    clients[0] = new Client(server_socket.accept());
                    System.out.println("First user connected.");
                    clients[0].Write("0:Waiting for second user...");

                    clients[1] = new Client(server_socket.accept());
                    System.out.println("Second user connected."); 
                    clients[0].Write("0:Both users connected.");
                    clients[1].Write("0:Both users connected.");
                                                                    // Отправка пользователям ID
                    clients[0].Write("1:0");
                    clients[1].Write("1:1");
                    alive = true;
                    while(alive) {                                  // Цикл текущей беседы
                        String msg = clients[turn].Read();

                        if(msg.equals("/q")){                       // Команда для окончания "/q"
                            clients[0].Write("3:");
                            clients[0].Close();
                            clients[1].Write("3:");
                            clients[1].Close();
                            System.out.println("Conversation ended.");
                            alive = false;
                        } else {
                            if(turn == 0) {
                                turn = 1;
                            } else {
                                turn = 0;
                            }
                            clients[turn].Write("2:" + msg);
                        }
                    }  
                }
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
        Server s = new Server();
        s.StartServer();
    }
}
