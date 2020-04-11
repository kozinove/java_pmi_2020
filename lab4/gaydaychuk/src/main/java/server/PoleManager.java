package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.Message;

import java.io.IOException;
import java.util.ArrayList;

public class PoleManager {
    int yesVoted = 0;
    int noVoted = 0;
    boolean isPole = false;
    int mustToVote = 0;
    int votersCount;
    String getString(String s, ClientThread sender, ArrayList<ClientThread> all_clients){
        ObjectMapper mapper = new ObjectMapper();
        Message m = new Message();
        try {
            m = mapper.readValue(s, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!isPole){
            if(m.getPoleMark().equals("true")){
                isPole = true;
                mustToVote = votersCount = all_clients.size();
                return "POLE BEGAN:" + m.getText();
            }
        } else {
            if (m.getVoteMark().equals("true")) {
                if (m.getText().equals("yes")) {
                        yesVoted++;
                        mustToVote--;
                } else if (m.getText().equals("no")) {
                        noVoted++;
                        mustToVote--;
                } else {
                    return "";
                }

                if (mustToVote == 0) {
                    isPole = false;
                    return "Yes: " + Integer.toString(yesVoted) + "/" + Integer.toString(votersCount) +
                            "\nNo" + Integer.toString(noVoted) + "/" + Integer.toString(votersCount);
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }

        return  Integer.toString(getIndex(sender, all_clients)) + " client send:\t" + m.getText();
    }
    private int getIndex(ClientThread sender, ArrayList<ClientThread> all_clients){
        int c = 0;
        for (int i = 0; i < all_clients.size(); i ++) {
            if(all_clients.get(i) == sender){
                c = i;
            }
        }
        return c;
    }
}
