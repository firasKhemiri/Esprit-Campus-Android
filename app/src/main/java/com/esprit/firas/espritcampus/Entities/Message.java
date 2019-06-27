package com.esprit.firas.espritcampus.Entities;

/**
 * Created by FIRAS on 05/05/2017.
 */

public class Message {

    private int id;
    private int Sender;
    private int reciever;

    private String createdAt;
    private String message;

    private int my_id;

    private boolean seen;

    private int convo_id;

    private String conImg;


    public String getConImg() {
        return conImg;
    }

    public void setConImg(String conImg) {
        this.conImg = conImg;
    }

    public int getConvo_id() {
        return convo_id;
    }

    public void setConvo_id(int convo_id) {
        this.convo_id = convo_id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return Sender;
    }

    public void setSender(int sender) {
        Sender = sender;
    }

    public int getReciever() {
        return reciever;
    }

    public void setReciever(int reciever) {
        this.reciever = reciever;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMy_id() {
        return my_id;
    }

    public void setMy_id(int my_id) {
        this.my_id = my_id;
    }
}
