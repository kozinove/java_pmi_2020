public class Message {
    String type;
    String value;

    Message(String type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
