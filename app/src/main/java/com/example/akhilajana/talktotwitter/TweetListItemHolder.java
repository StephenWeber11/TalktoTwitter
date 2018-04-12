package com.example.akhilajana.talktotwitter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by akhilajana on 4/5/18.
 */

public class TweetListItemHolder extends RecyclerView.ViewHolder {

    TextView userName, tweetInfo, tweetTime;
    ImageView userProfilePic;

    public TweetListItemHolder(View itemView) {
        super(itemView);


    }
}
