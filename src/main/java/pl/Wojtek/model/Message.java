package pl.Wojtek.model;


public class Message extends Action {
    private String content;

    public Message() {}

    public Message(String s) {
        this.content = s;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
