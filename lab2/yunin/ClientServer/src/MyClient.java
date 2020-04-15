import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream()); )
        {
            while(true){
                String response = ois.readUTF();
                if(response.equals(("q"))){
                    System.out.println("Other user disconnected");
                    break;
                }
                System.out.println(response);
                System.out.println("Say something...");
                String clientCommand = br.readLine();
                oos.writeUTF(clientCommand);
                if(clientCommand.equals(("q"))){
                    System.out.println("You disconnected");
                    break;
                }
                oos.flush();
                Thread.sleep(1000);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}