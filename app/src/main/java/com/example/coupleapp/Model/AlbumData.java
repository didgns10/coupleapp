package com.example.coupleapp.Model;

public class AlbumData {
    private String albumtitle;
    private String albumtitleimg;

    public AlbumData(String albumtitle, String albumtitleimg) {
        this.albumtitle = albumtitle;
        this.albumtitleimg = albumtitleimg;
    }

    public AlbumData() {
    }

    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public String getAlbumtitleimg() {
        return albumtitleimg;
    }

    public void setAlbumtitleimg(String albumtitleimg) {
        this.albumtitleimg = albumtitleimg;
    }
}
