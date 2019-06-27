package com.esprit.firas.espritcampus.Entities;

/**
 * Created by FIRAS on 05/05/2017.
 */

public class Comments {

    private int id;
    private int user_id;
    private boolean is_social;
    private String post;
    private String createdAt;
    private String date_modified;
    private String comment;
    private String photoprof;
    private String name;
    private int myid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public boolean is_social() {
        return is_social;
    }

    public void setIs_social(boolean is_social) {
        this.is_social = is_social;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoprof() {
        return photoprof;
    }

    public void setPhotoprof(String photoprof) {
        this.photoprof = photoprof;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }
}
