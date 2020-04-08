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

public class Client {

    private static Socket client;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static class Read extends Thread {

        public Read() {
            start();
        }

        ;
     
    public void run() {

            String answer;
            try {
                while (true) {
                    answer = in.readLine();

                    if (answer.contains("?")) {
                        System.out.println("Answer[y/n]: ");
                    }

                    if (answer.contains("?") || answer.contains("#RES")) {
                        System.out.println(answer);
                    }

                    if (answer.equals("q")) {

                        break; 
                    }
                }
            } catch (IOException e) {

            }
        }
    }

    public static class Write extends Thread {

        public Write() {
            start();
        }

        ;
    public void run() {
            while (true) {
                String word;
                try {
                    word = reader.readLine();
                    if (word.equals("q")) {
                        out.write("q" + "\n");
                        break;
                    } else {
                        out.write(word + "\n");
                    }
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
            String word = "";

            Read r = new Read();
            Write w = new Write();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
