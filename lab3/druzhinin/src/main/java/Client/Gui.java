package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                if (cst != null) {
                    cst.send(textField1.getText());
                }
            }
        });

        // vote pro
        VotePro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && cst.voting && !cst.already_voted) {
                    cst.send("vote:pro");
                    cst.imVoted();
                }
            }
        });

        // vote con
        VoteCon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && cst.voting && !cst.already_voted) {
                    cst.send("vote:con");
                    cst.imVoted();
                }
            }
        });

        // start voting
        Voting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null && !cst.voting) {
                    cst.send("voting: " + textField1.getText());
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
