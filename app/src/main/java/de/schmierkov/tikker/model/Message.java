package de.schmierkov.tikker.model;

public class Message {
    public int id;
    public String text;
    public String date;
    public String user;

    public Message(){}

    public Message(String user, String text) {
        super();
        this.user = user;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", user=" + user + ", text=" + text + "]";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
