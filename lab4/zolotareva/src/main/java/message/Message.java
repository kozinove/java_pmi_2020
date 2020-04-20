package message;

/**
 *
 * @author Олеся
 */
public class Message {
    String type;
    String message;
    
    public Message(String ttype, String mess) {
        type = ttype;
        message = mess;
    }
    
    public String getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
}
