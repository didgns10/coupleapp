package com.example.coupleapp.Model;

public class MessageData {
    private String message;
    private String image;
    private String couple_idx;
    private String message_idx;
    private String opp_profile_img;
    private String datetime;
    private String name;
    private String server;
    private String email;


    public MessageData(String message, String image, String couple_idx, String message_idx, String opp_profile_img, String datetime, String name, String server, String email) {
        this.message = message;
        this.image = image;
        this.couple_idx = couple_idx;
        this.message_idx = message_idx;
        this.opp_profile_img = opp_profile_img;
        this.datetime = datetime;
        this.name = name;
        this.server = server;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public MessageData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOpp_profile_img() {
        return opp_profile_img;
    }

    public void setOpp_profile_img(String opp_profile_img) {
        this.opp_profile_img = opp_profile_img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCouple_idx() {
        return couple_idx;
    }

    public void setCouple_idx(String couple_idx) {
        this.couple_idx = couple_idx;
    }

    public String getMessage_idx() {
        return message_idx;
    }

    public void setMessage_idx(String message_idx) {
        this.message_idx = message_idx;
    }
}
