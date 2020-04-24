/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.mychat.message;

/**
 *
 * @author J-win
 */
public class JMessage {
    String type;
    String message;

    public JMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
