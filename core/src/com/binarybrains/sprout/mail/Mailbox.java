package com.binarybrains.sprout.mail;

import java.util.ArrayList;
import java.util.List;


public class Mailbox {

    public List<Message> messages = new ArrayList<Message>();

    public void add(Message message) {
        // todo indicate to stats that the player recieved the mail
        messages.add(message);
    }

    public boolean gotMail() {
        return messages.size() > 0;
    }

    public Message read() {
        if (!gotMail()) return null;
        // todo indicate stats that the player read the mail
        return messages.remove(0);
    }
}
