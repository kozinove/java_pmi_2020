package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.Message;

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
    private JTextField targetId;
    private JTextField targetText;
    private JButton TargetSend;

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
                ObjectMapper mapper = new ObjectMapper();
                Message m = new Message();
                m.setText(textField1.getText());
                String stringToSend = "default";
                try {
                    stringToSend = mapper.writeValueAsString(m);
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }
                if (cst != null) {
                    cst.send(stringToSend);
                }
            }
        });
        VoteYes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectMapper mapper = new ObjectMapper();
                Message m = new Message();
                m.setVoteMark("true");
                m.setText("yes");
                String stringToSend = "default";
                try {
                    stringToSend = mapper.writeValueAsString(m);
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }
                if (cst != null) {
                    cst.send(stringToSend);
                }
            }
        });
        VoteNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectMapper mapper = new ObjectMapper();
                Message m = new Message();
                m.setVoteMark("true");
                m.setText("no");
                String stringToSend = "default";
                try {
                    stringToSend = mapper.writeValueAsString(m);
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }
                if (cst != null) {
                    cst.send(stringToSend);
                }
            }
        });
        SubmitPole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectMapper mapper = new ObjectMapper();
                Message m = new Message();
                m.setPoleMark("true");
                m.setText(textField1.getText());
                String stringToSend = "default";
                try {
                    stringToSend = mapper.writeValueAsString(m);
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }
                if (cst != null) {
                    cst.send(stringToSend);
                }
            }
        });
        TargetSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectMapper mapper = new ObjectMapper();
                Message m = new Message();
                m.setTargetId(targetId.getText());
                m.setTargetMark("true");
                m.setText(targetText.getText());
                String stringToSend = "default";
                try {
                    stringToSend = mapper.writeValueAsString(m);
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }
                if (cst != null) {
                    cst.send(stringToSend);
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
