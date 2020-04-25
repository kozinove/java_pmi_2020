import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JButton connectButton;
    private JButton yesButton;
    private JButton noButton;
    private JTextField suggest_field;
    private JButton suggestButton;
    private JLabel resullt_voting;
    private JPanel MyPanel;
    private JButton showHistoryButton;

    MyClient client;

    public GUI() {
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (client == null) {
                    client = new MyClient(GUI.this);
                    resullt_voting.setText("Wait to suggest...");
                }
            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null && client.can_vote) {
                    client.can_vote = false;
                    client.send(client.gson.toJson(new Message("VOTE","-")));
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null && client.can_vote) {
                    client.can_vote = false;
                    client.send(client.gson.toJson(new Message("VOTE","+")));
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
        suggestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null && client.can_suggest) {
                    String s = suggest_field.getText();
                    client.send(client.gson.toJson(new Message("SUGGEST",s)));
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
    }


    public void set_result(String s){
        resullt_voting.setText(s);
    }
    public void show_history(String h){
        if (h.equals("")){
            return;
        }
        JFrame history = new JFrame("History");
        history.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        history.add(new JTextArea("Question| Yes| No"+ "\n"+h));
        history.pack();
        history.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Voting Application");
        frame.setContentPane(new GUI().MyPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

