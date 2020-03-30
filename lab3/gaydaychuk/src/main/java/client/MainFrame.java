package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {


    private JPanel MyPanel;
    private JTextArea textArea1;
    private JButton Send;
    private JTextField textField1;
    private JButton Connect;
    private JButton VoteYes;
    private JButton VoteNo;
    private JButton SubmitPole;

    ClientServerThread cst;

    public MainFrame() {
        Connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst == null) {
                    cst = new ClientServerThread(MainFrame.this);
                }
            }
        });
        Send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null) {
                    cst.send(textField1.getText());
                }
            }
        });
        VoteYes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null) {
                    cst.send("vote:yes");
                }
            }
        });
        VoteNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null) {
                    cst.send("vote:no");
                }
            }
        });
        SubmitPole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cst != null) {
                    cst.send("pole:" + textField1.getText());
                }
            }
        });
    }

    public void  addTextToFrame(String s)
    {
        String str = textArea1.getText();
        str += s + "\n";
        textArea1.setText(str);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainFrame");
        frame.setContentPane(new MainFrame().MyPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
