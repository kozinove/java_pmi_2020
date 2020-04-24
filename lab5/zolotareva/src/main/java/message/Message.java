package message;

/**
 *
 * @author Олеся
 */
public class Message {
    String type;
    String message;
    
    public Message(String t, String m) {
        type = t;
        message = m;
    }
    
    public void setID(int id){
        message = "# " + Integer.toString(id) + ": " + message;
    }
    public String getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
}
