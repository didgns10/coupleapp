package com.example.coupleapp.Model;

public class AlbumViewData {

    private String img_idx;
    private String img;
    private String album;

    public AlbumViewData(String img_idx, String img, String album) {
        this.img_idx = img_idx;
        this.img = img;
        this.album = album;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public AlbumViewData() {
    }

    public String getImg_idx() {
        return img_idx;
    }

    public void setImg_idx(String img_idx) {
        this.img_idx = img_idx;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
