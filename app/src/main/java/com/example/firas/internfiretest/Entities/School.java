package com.example.firas.internfiretest.Entities;


public class School {

        private int id;
        private String name;
        private int cont_id;
        private int owner_id;
        private int owner_name;
        private String photoprof;
        private String photocouv;
        private Boolean ibelong;
        private int students;


    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public Boolean getIbelong() {
        return ibelong;
    }

    public void setIbelong(Boolean ibelong) {
        this.ibelong = ibelong;
    }

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

    public int getCont_id() {
        return cont_id;
    }

    public void setCont_id(int cont_id) {
        this.cont_id = cont_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(int owner_name) {
        this.owner_name = owner_name;
    }

    public String getPhotoprof() {
        return photoprof;
    }

    public void setPhotoprof(String photoprof) {
        this.photoprof = photoprof;
    }

    public String getPhotocouv() {
        return photocouv;
    }

    public void setPhotocouv(String photocouv) {
        this.photocouv = photocouv;
    }
}
