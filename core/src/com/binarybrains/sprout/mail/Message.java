package com.binarybrains.sprout.mail;

public class Message {
    public String title;
    public String body;
    // more stuff?

    public Message(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

}
