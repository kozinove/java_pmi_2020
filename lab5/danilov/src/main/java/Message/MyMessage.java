package Message;

public class MyMessage {
    String type;
    String name;
    String message;
    String dest;

    public MyMessage(String t, String n, String d, String m) {
        type = t;
        name = n;
        dest = d;
        message = m;
    }
    public MyMessage(String t, String n, String m) {
        type = t;
        name = n;
        message = m;
    }
    public MyMessage(String t, String m) {
        type = t;
        message = m;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return dest;
    }

    public String getMessage() {
        return message;
    }
}
