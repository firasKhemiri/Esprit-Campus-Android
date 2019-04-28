package com.example.firas.internfiretest.Entities;

/**
 * Created by FIRAS on 05/05/2017.
 */

public class Pending {

    private int id;
    private String createdAt;
    private String message;
    private int myid;
    private User user;
    private int dep_id;
    private String dep_name;
    private int classe_id;
    private String classe_name;
    private boolean is_prof;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDep_id() {
        return dep_id;
    }

    public void setDep_id(int dep_id) {
        this.dep_id = dep_id;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public int getClasse_id() {
        return classe_id;
    }

    public void setClasse_id(int classe_id) {
        this.classe_id = classe_id;
    }

    public String getClasse_name() {
        return classe_name;
    }

    public void setClasse_name(String classe_name) {
        this.classe_name = classe_name;
    }

    public boolean is_prof() {
        return is_prof;
    }

    public void setIs_prof(boolean is_prof) {
        this.is_prof = is_prof;
    }
}
