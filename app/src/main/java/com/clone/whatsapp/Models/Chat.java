package com.clone.whatsapp.Models;

public class Chat {

    public static final int TEXT_MESSAGE = 0;
    public static final int IMAGE_MESSAGE = 1;


    private String dateTime;
    private String textMessage;
    private int type;
    private String receiver;
    private String sender;

    public Chat(String dateTime, String textMessage, int type, String receiver, String sender) {
        this.dateTime = dateTime;
        this.textMessage = textMessage;
        this.type = type;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Chat() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
