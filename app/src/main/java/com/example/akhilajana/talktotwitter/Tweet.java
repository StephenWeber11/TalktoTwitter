package com.example.akhilajana.talktotwitter;

import java.util.Date;

/**
 * Created by Stephen on 4/5/2018.
 */

public class Tweet {

    Date createdAt;
    User user;
    String tweetContent;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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
