package com.example.firas.internfiretest.Entities;

/**
 * Created by FIRAS on 05/05/2017.
 */

public class Category {

    private int id;
    private String  name;
    private String description;
    private String pic_url;

    private int Creator_id;
    private String Creator_name;
    private String date;
    private int num_posts;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator_id() {
        return Creator_id;
    }

    public void setCreator_id(int creator_id) {
        Creator_id = creator_id;
    }

    public String getCreator_name() {
        return Creator_name;
    }

    public void setCreator_name(String creator_name) {
        Creator_name = creator_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum_posts() {
        return num_posts;
    }

    public void setNum_posts(int num_posts) {
        this.num_posts = num_posts;
    }
}
