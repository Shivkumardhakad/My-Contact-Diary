package com.smart.helper;

public class Message {

    private String content;   // ADD THIS FIELD
    private String type;

    public Message(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
