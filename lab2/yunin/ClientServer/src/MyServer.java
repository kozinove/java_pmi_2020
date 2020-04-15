import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        try (ServerSocket server=new ServerSocket(3345)){
            Socket first_client = server.accept();
            System.out.print("First connected.\n");
            Socket second_client = server.accept();
            System.out.print("Second connected.\n");
            DataOutputStream first_out = new DataOutputStream(first_client.getOutputStream());
            DataOutputStream second_out = new DataOutputStream(second_client.getOutputStream());
            DataInputStream first_in = new DataInputStream(first_client.getInputStream());
            DataInputStream second_in = new DataInputStream(second_client.getInputStream());
            System.out.println("DataInputStream created\n");
            boolean first = true;
            boolean second = false;

            first_out.writeUTF("You write first.");
            first_out.flush();
            while(!first_client.isClosed() && !second_client.isClosed()){
                if(first) {
                    String entry = first_in.readUTF();
                    System.out.println("First says: "+entry);
                    second_out.writeUTF(entry);
                    if(entry.equals(("q"))){
                        System.out.println("First user disconnected");
                        break;
                    }
                    first = false;
                    second = true;
                }
                if(second){
                    String entry = second_in.readUTF();
                    System.out.println("Second says: "+entry);
                    first_out.writeUTF(entry);
                    if(entry.equals(("q"))){
                        System.out.println("Second user disconnected");
                        break;
                    }
                    second = false;
                    first = true;
                }
            }

            System.out.println("Closing connections & channels.");

            first_in.close();
            first_out.close();

            second_in.close();
            second_out.close();

            first_client.close();
            second_client.close();
            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}