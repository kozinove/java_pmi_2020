package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Message.MyMessage;

public class Gui {
	
	private JPanel MyPanel;
    private JTextArea textArea1;
    private JButton Send;
    private JTextField textField1;
    private JButton Connect;
    private JButton VotePro;
    private JButton VoteCon;
    private JButton Voting;
    private JLabel label_1;
    private JLabel label_2;

    ClientServerThread cst;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Gui() {
        // connect to server, create client-server thread
        Connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst == null) {
                    cst = new ClientServerThread(Gui.this);
                }
            }
        });

        // send the message to server
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && !cst.voting) {
                    MyMessage m = new MyMessage("bcast", textField1.getText());
                    cst.send(gson.toJson(m));
                }
            }
        });

        // vote pro
        VotePro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && cst.voting && !cst.already_voted) {
                    MyMessage m = new MyMessage("answer", "pro");
                    cst.send(gson.toJson(m));
                    cst.imVoted();
                }
            }
        });

        // vote con
        VoteCon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && cst.voting && !cst.already_voted) {
                    MyMessage m = new MyMessage("answer", "con");
                    cst.send(gson.toJson(m));
                    cst.imVoted();
                }
            }
        });

        // start voting
        Voting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && !cst.voting) {
                    MyMessage m = new MyMessage("voting", "Voting: " + textField1.getText());
                    cst.send(gson.toJson(m));
                    cst.startVoting();
                }
            }
        });
	}

    public void UpdateText(String s)
    {
        String str = textArea1.getText();
        str += s + "\n";
        textArea1.setText(str);
    }
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Window");
        frame.setContentPane(new Gui().MyPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
}
