package com.example.akhilajana.talktotwitter;

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

    ArrayList<Tweets> tweetsList;

    public TweetsAdapter(ArrayList<Tweets> tweetsList)
    {
        this.tweetsList = tweetsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_list_item, parent, false);
        TweetListItemHolder holder=new TweetListItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }
}

