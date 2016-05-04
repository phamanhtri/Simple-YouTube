package com.example.phama.youtube;

/**
 * Created by phama on 10/4/2016.
 */
public class    Video {
    public  int id;
    public String videoId;
    public String title;
    public String url;



    public Video(int id,String videoId, String title, String url) {
        this.id = id;
        this.title = title;
        this.videoId = videoId;
        this.url = url;

    }
    public  int getId(){return  id;}

    public void setId(int id){this.id = id;}

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
