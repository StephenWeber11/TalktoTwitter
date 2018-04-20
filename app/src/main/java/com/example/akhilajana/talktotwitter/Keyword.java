package com.example.akhilajana.talktotwitter;

import java.util.ArrayList;

/**
 * Created by Stephen on 4/19/2018.
 */

public class Keyword {
    private String keyword;
    private ArrayList<Tweet> tweets;

    public Keyword(){}

    public Keyword(String keyword, ArrayList<Tweet> tweets) {
        this.keyword = keyword;
        this.tweets = tweets;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }
}
