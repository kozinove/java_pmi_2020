package shared;

public class Message {

    private String text;  // String
    private String targetId;  // int

    private String targetMark;  // bool
    private String voteMark;  // bool
    private String poleMark;  // bool

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message() {
        this.targetMark = "false";
        this.voteMark = "false";
        this.poleMark = "false";
        this.text = "";
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetMark() {
        return targetMark;
    }

    public void setTargetMark(String targetMark) {
        this.targetMark = targetMark;
    }

    public String getVoteMark() {
        return voteMark;
    }

    public void setVoteMark(String voteMark) {
        this.voteMark = voteMark;
    }

    public String getPoleMark() {
        return poleMark;
    }

    public void setPoleMark(String poleMark) {
        this.poleMark = poleMark;
    }

}
