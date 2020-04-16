/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unn.voting_system.message;

/**
 *
 * @author user
 */
public class Message {
    String type;
    String message;
    
    public Message(String t, String m) {
        type = t;
        message = m;
    }
    
    public String getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
}
