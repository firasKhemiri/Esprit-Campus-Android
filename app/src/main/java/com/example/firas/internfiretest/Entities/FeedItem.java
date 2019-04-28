package com.example.firas.internfiretest.Entities;


public class FeedItem {

    private int id;
    private String post_name;
    private String post_desc;
    private int num_comments;
    private int num_likes;
    private boolean liked;
    private boolean is_picture;
    private boolean is_question;
    private String pic_url;
    private Category category;


    private String username;
    private String user_flname;

    private boolean is_social;

    private String photoprof;
    private int user_id;

    private int idme;

    private String date;

    private String showbeg;
    private String showend;


    private int participants;
    private int invited;
    private String location;
    private String dateBeg;
    private String dateEnd;

    private int type;

    private String filePath;

    private String cat_name;


    private int size;

    private boolean participated;



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean is_social() {
        return is_social;
    }

    public void setIs_social(boolean is_social) {
        this.is_social = is_social;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getPost_desc() {
        return post_desc;
    }

    public void setPost_desc(String post_desc) {
        this.post_desc = post_desc;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean is_picture() {
        return is_picture;
    }

    public void setIs_picture(boolean is_picture) {
        this.is_picture = is_picture;
    }

    public boolean is_question() {
        return is_question;
    }

    public void setIs_question(boolean is_question) {
        this.is_question = is_question;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
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

    public String getPhotoprof() {
        return photoprof;
    }

    public void setPhotoprof(String photoprof) {
        this.photoprof = photoprof;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIdme() {
        return idme;
    }

    public void setIdme(int idme) {
        this.idme = idme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShowbeg() {
        return showbeg;
    }

    public void setShowbeg(String showbeg) {
        this.showbeg = showbeg;
    }

    public String getShowend() {
        return showend;
    }

    public void setShowend(String showend) {
        this.showend = showend;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getInvited() {
        return invited;
    }

    public void setInvited(int invited) {
        this.invited = invited;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateBeg() {
        return dateBeg;
    }

    public void setDateBeg(String dateBeg) {
        this.dateBeg = dateBeg;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isParticipated() {
        return participated;
    }

    public void setParticipated(boolean participated) {
        this.participated = participated;
    }
}
