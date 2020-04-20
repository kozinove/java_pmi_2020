package DB;

public class Message {
    private String senderId;
    private String text;
    private int id;

    public String toString(){
        return "sender " + senderId + ": " + text;
    }

    public Message(int id, String senderId, String text) {
        this.senderId = senderId;
        this.text = text;
        this.id = id;
    }

    public Message(String senderId, String text) {
        this.senderId = senderId;
        this.text = text;
        this.id = -1;
    }

    public String getSenderId() {

        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
