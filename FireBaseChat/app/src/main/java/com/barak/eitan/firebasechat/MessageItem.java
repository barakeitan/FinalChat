package com.barak.eitan.firebasechat;

import java.util.Date;

/**
 * Created by barak on 14/03/2017.
 */

public class MessageItem {
    private String Sender;
    private String Message;
    private Date date;

    public MessageItem(){

    }

    public MessageItem(String sender, String message, Date date) {
        Sender = sender;
        Message = message;
        this.date = date;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}