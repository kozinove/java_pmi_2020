import javax.swing.*;
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
                    client.send("-");
                    client.can_vote = false;
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null && client.can_vote) {
                    client.send("+");
                    client.can_vote = false;
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
        suggestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null && client.can_suggest) {
                    String s = suggest_field.getText();
                    client.send("SUGGEST@" + s);
                    resullt_voting.setText("Waiting for others...");
                }
            }
        });
    }


    public void set_result(String s){
        resullt_voting.setText(s);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Voting Application");
        frame.setContentPane(new GUI().MyPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

