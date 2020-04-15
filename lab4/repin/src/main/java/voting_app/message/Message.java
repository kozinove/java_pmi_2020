/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting_app.message;

/**
 *
 * @author mezotaken
 */
public class Message {
    private String type;  
    private String text;
    
    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }
    public String Type() {
        return type;
    }
    public String Text() {
        return text;
    }
}
