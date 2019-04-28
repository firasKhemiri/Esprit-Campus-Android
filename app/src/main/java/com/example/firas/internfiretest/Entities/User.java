package com.example.firas.internfiretest.Entities;


public class User {

    private int id;
    private int my_id;

    private String username;
    private String user_flname;

    private boolean is_social;

    private String profile_pic;
    private String cover_pic;


    private String classe;
    private String department;

    private Boolean is_me;

    private Boolean is_admin;
    private Boolean is_prof;

    private String bio;
    private String email;

    private boolean ifollowuser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_flname() {
        return user_flname;
    }

    public void setUser_flname(String user_flname) {
        this.user_flname = user_flname;
    }

    public boolean is_social() {
        return is_social;
    }

    public void setIs_social(boolean is_social) {
        this.is_social = is_social;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }


    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getIs_me() {
        return is_me;
    }

    public void setIs_me(Boolean is_me) {
        this.is_me = is_me;
    }

    public boolean isIfollowuser() {
        return ifollowuser;
    }

    public void setIfollowuser(boolean ifollowuser) {
        this.ifollowuser = ifollowuser;
    }

    public int getMy_id() {
        return my_id;
    }

    public void setMy_id(int my_id) {
        this.my_id = my_id;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Boolean getIs_prof() {
        return is_prof;
    }

    public void setIs_prof(Boolean is_prof) {
        this.is_prof = is_prof;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
