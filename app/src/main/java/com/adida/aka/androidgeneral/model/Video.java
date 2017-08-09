package com.adida.aka.androidgeneral.model;

/**
 * Created by tmha on 8/9/2017.
 */

public class Video {
    private String mIdVideo;
    private String mTitle;
    private String mChannelTitle;
    private String mImage;

    public Video() {
    }

    public Video(String mIdVideo, String mTitle, String mChannelTitle, String mImage) {
        this.mIdVideo = mIdVideo;
        this.mTitle = mTitle;
        this.mChannelTitle = mChannelTitle;
        this.mImage = mImage;
    }

    public String getmIdVideo() {
        return mIdVideo;
    }

    public void setmIdVideo(String mIdVideo) {
        this.mIdVideo = mIdVideo;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmChannelTitle() {
        return mChannelTitle;
    }

    public void setmChannelTitle(String mChannelTitle) {
        this.mChannelTitle = mChannelTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
