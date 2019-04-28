package com.example.firas.internfiretest.Entities;


import java.util.ArrayList;

public class Event extends FeedItem {


    private int num_participants;
    private String location;
    private String dateBeg;
    private String dateEnd;
    private ArrayList<User> particpants;
    private boolean participated;
    private boolean isLimited;
    private boolean reachedLimit;
    private int maxLimit;

    private Double lat;
    private Double lng;



    public ArrayList<User> getParticpants() {
        return particpants;
    }

    public void setParticpants(ArrayList<User> particpants) {
        this.particpants = particpants;
    }

    public int getNum_participants() {
        return num_participants;
    }

    public void setNum_participants(int num_participants) {
        this.num_participants = num_participants;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public boolean isReachedLimit() {
        return reachedLimit;
    }

    public void setReachedLimit(boolean reachedLimit) {
        this.reachedLimit = reachedLimit;
    }

    public void setLng(Double lng) {
        this.lng = lng;
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

    public boolean isParticipated() {
        return participated;
    }

    public void setParticipated(boolean participated) {
        this.participated = participated;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }
}
