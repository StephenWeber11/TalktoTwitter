package com.example.akhilajana.talktotwitter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class TweetsAdapter extends RecyclerView.Adapter{

    ArrayList<Tweet> tweetsList;
    Tweet tweet;
    Activity activity;

    public TweetsAdapter(ArrayList<Tweet> tweetsList, Activity activity)
    {
        this.tweetsList = tweetsList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_list_item, parent, false);
        TweetListItemHolder holder = new TweetListItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TweetListItemHolder holderObj = (TweetListItemHolder) holder;
        tweet = tweetsList.get(position);
        holderObj.userName.setText(tweet.getUser().getName());
        holderObj.tweetInfo.setText(tweet.getTweetContent());
        holderObj.tweetTime.setText(tweet.getCreatedAt());

        Picasso.with(activity).load(tweet.getUser().getImageUrl()).into(((TweetListItemHolder) holder).userProfilePic);
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }
}

