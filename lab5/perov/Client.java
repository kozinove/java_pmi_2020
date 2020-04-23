/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author perov
 */
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Message {

    String message;
    String id;
    boolean question;

    void Set(String my_id, String my_str) {
        id = my_id;
        message = my_str;

        if (message.contains("?")) {
            question = true;
        } else {
            question = false;
        }

    }

    Message(String my_id, String my_str) {
        id = my_id;
        message = my_str;
    }
}

public class Client {

    private static Gson gson;
    private static Message from_me;
    private static Message to_me;
    private static String id;

    private static Socket client;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static class Read extends Thread {

        public Read() {
            start();
        }

        public void run() {

            try {

                while (true) {
                    String answer = "";

                    int ch;
                    long i = 0;

                    while ((ch = in.read()) >= 0) {
                        char c = (char) ch;

                        answer += c;
                        if (c == '}') {
                            break;
                        }

                    }

                    to_me = gson.fromJson(answer, Message.class);

                    if (to_me.question) {
                        System.out.println("Answer[y/n]: ");
                    }

                    System.out.println(answer);

                }
            } catch (IOException e) {

            }
        }
    }

    public static class Write extends Thread {

        public Write() {
            start();
        }

        public void run() {
            while (true) {
                String word;
                try {
                    word = reader.readLine();

                    from_me.Set(id, word);

                    String obj = gson.toJson(from_me);

                    out.write(obj);

                    out.flush();
                } catch (IOException e) {

                }

            }
        }
    }

    public static void main(String[] args) {
        try {
            client = new Socket("localhost", 4004);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            gson = new GsonBuilder().setPrettyPrinting().create();
            Integer index = (int) (Math.random() * 500);
            Client.id = index.toString();
            from_me = new Message(Client.id, "Null");
            to_me = new Message(Client.id, "Null");

            Write w = new Write();
            Read r = new Read();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
