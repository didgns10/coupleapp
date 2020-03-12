package com.example.coupleapp.Model;

public class StoryThumData {

    private String storyThumbimg;
    private String storyday;
    private String storytitle;

    public String getStorytitle() {
        return storytitle;
    }

    public void setStorytitle(String storytitle) {
        this.storytitle = storytitle;
    }

    public StoryThumData() {
    }

    public StoryThumData(String storyThumbimg, String storyday, String storytitle) {
        this.storyThumbimg = storyThumbimg;
        this.storyday = storyday;
        this.storytitle = storytitle;
    }

    public String getStoryThumbimg() {
        return storyThumbimg;
    }

    public void setStoryThumbimg(String storyThumbimg) {
        this.storyThumbimg = storyThumbimg;
    }

    public String getStoryday() {
        return storyday;
    }

    public void setStoryday(String storyday) {
        this.storyday = storyday;
    }
}
