package server;

import java.util.ArrayList;

public class PoleManager {
    int yesVoted = 0;
    int noVoted = 0;
    boolean isPole = false;
    int mustToVote = 0;
    int votersCount;
    String getString(String s, ClientThread sender, ArrayList<ClientThread> all_clients){
        if(!isPole){
            if(s.length() >= 5 && s.substring(0, 4).equals("pole")){
                isPole = true;
                mustToVote = votersCount = all_clients.size();
                return "POLE BEGAN:" + s.substring(5, s.length());
            }
        } else {
            if (s.length() >= 5 && s.substring(0, 4).equals("vote")) {
                if (s.substring(5, s.length()).equals("yes")) {
                        yesVoted++;
                        mustToVote--;
                } else if (s.substring(5, s.length()).equals("no")) {
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

        return  Integer.toString(getIndex(sender, all_clients)) + " client send:\t" + s;
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
