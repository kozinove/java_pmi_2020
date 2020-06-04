package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Message.MyMessage;

public class Gui {
	
	private JPanel MyPanel;
    private JTextArea textArea1;
    private JButton Send;
    private JTextField textField1;
    private JButton Connect;
    private JLabel label_1;
    private JTextArea textArea2;
    private JLabel label_2;

    ClientServerThread cst;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String name = new String();

    public Gui() {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                String add = gson.toJson(new MyMessage("remove", name, ""));
                cst.send(add);
            }
        });
        // connect to server, create client-server thread
        Connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (name.isEmpty()) {
                    name = JOptionPane.showInputDialog(
                            MyPanel,
                            "<html><h2>Hello, enter your nickname");
                }
                if (cst == null) {
                    cst = new ClientServerThread(Gui.this);
                }
            }
        });

        // send the message to server
        Send.setToolTipText("/w <user> <message>. Sends the private message <message> to <user>.");
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null) {
                    MyMessage m;
                    if (textField1.getText().contains("/w")) {
                        String s = textField1.getText();
                        String dest = new String();
                        String message = new String();
                        Pattern pattern = Pattern.compile("(?<=w )([^ ]*) (.*)");
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            dest = matcher.group(1);
                            message = matcher.group(2);
                        }

                        m = new MyMessage("whisper", name, dest, message);
                        UpdateText("[" + name + "] -> [" + dest + "]: " + message);
                    } else {
                        m = new MyMessage("bcast", name, textField1.getText());
                    }
                    cst.send(gson.toJson(m));
                }
                textField1.setText("");
            }
        });
	}

    public void UpdateText(String s)
    {
        String chat = textArea1.getText();
        chat += s;
        if (!s.endsWith("\n")) {
            chat += '\n';
        }

        textArea1.setText(chat);
    }
    public void UpdateUsers(String s)
    {
        String users = s.replace(name, "> " + name);
        textArea2.setText(users);
    }
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("BTWChat");
        frame.setContentPane(new Gui().MyPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
}
