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
    String message;
    int id;

    Letter(String _message) {
        message = _message;
        id = -1;
    }
    Letter(String _message, int _id) {
        message = _message;
        id = _id;
    }
    Letter() {
        message = "";
        id = -1;
    }
    boolean thisIsAnswer()
    {
        return (message.equals("yes") || message.equals("no"));
    }
    boolean startVote()
    {
        return (message.equals("Start_vote"));
    }
    boolean positive()
    {
        return (message.equals("yes"));
    }
    boolean negative()
    {
        return (message.equals("no"));
    }
}
