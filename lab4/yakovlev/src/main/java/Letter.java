/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavel
 */
public class Letter {
    private String message;
    int id;
    int answer;

    Letter(String _message) {
        message = _message;
        id = -1;
        parseAns();
    }
    Letter(String _message, int _id) {
        message = _message;
        id = _id;
        parseAns();
    }
    void setMes(String m)
    {
        message = m;
        parseAns();
    }
    String getMes()
    {
        return message;
    }
    Letter() {
        message = "";
        id = -1;
        parseAns();
    }
    /*boolean thisIsAnswer()
    {
        return (message.equals("yes") || message.equals("no"));
    }*/
    boolean startVote()
    {
        return (message.equals("Start_vote"));
    }
    /*boolean positive()
    {
        return (message.equals("yes"));
    }
    boolean negative()
    {
        return (message.equals("no"));
    }*/
    private void parseAns()
    {
        if (message.equals("yes")) answer = 1;
        else if (message.equals("no")) answer = 0;
        else answer = -1;
    }
}
