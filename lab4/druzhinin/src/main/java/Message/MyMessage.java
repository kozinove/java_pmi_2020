package Message;

public class MyMessage {
    String type;
    String message;

    public MyMessage(String t, String m) {
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
