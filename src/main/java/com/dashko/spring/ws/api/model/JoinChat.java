package com.dashko.spring.ws.api.model;

import java.util.List;

public class JoinChat {
    private String sender;
    private MessageType type;
    List<ChatMessage> messages;

    public JoinChat(String sender, MessageType type, List<ChatMessage> messages) {
        this.sender = sender;
        this.type = type;
        this.messages = messages;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
