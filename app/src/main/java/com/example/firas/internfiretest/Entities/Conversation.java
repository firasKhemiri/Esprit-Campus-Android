package com.example.firas.internfiretest.Entities;

import java.sql.Date;


public class Conversation {

    private int conversation_id;
    private int msg_id;
    private String message;
    private int cont_id;
    private int sender;
    private int reciever;
    private String cont_name;
    private String cont_image;
    private boolean cont_social;
    private boolean seen;
    private int my_id;
    private Date date;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    private String dateStr;

    public int getConversation_id() {
        return conversation_id;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCont_id() {
        return cont_id;
    }

    public void setCont_id(int cont_id) {
        this.cont_id = cont_id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReciever() {
        return reciever;
    }

    public void setReciever(int reciever) {
        this.reciever = reciever;
    }

    public String getCont_name() {
        return cont_name;
    }

    public void setCont_name(String cont_name) {
        this.cont_name = cont_name;
    }

    public String getCont_image() {
        return cont_image;
    }

    public void setCont_image(String cont_image) {
        this.cont_image = cont_image;
    }

    public boolean isCont_social() {
        return cont_social;
    }

    public void setCont_social(boolean cont_social) {
        this.cont_social = cont_social;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getMy_id() {
        return my_id;
    }

    public void setMy_id(int my_id) {
        this.my_id = my_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
