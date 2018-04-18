package com.example.akhilajana.talktotwitter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Stephen on 4/5/2018.
 */

public class Tweet implements Serializable{

    String createdAt;
    User user;
    String tweetContent;

    public Tweet () {}

    public Tweet(String createdAt, User user, String tweetContent) {
        this.createdAt = createdAt;
        this.user = user;
        this.tweetContent = tweetContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTweetContent() {
        return tweetContent;
    }

    public void setTweetContent(String tweetContent) {
        this.tweetContent = tweetContent;
    }
}
