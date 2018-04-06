package com.example.akhilajana.talktotwitter;

import java.util.ArrayList;

/**
 * Created by Stephen on 4/5/2018.
 */

public class TweetsList {
    ArrayList<Tweet> tweets;

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }
}
