package com.example.akhilajana.talktotwitter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by akhilajana on 4/5/18.
 */

public class TweetListItemHolder extends RecyclerView.ViewHolder {

    TextView userName, tweetInfo, tweetTime;
    ImageView userProfilePic;
    CardView layout;

    public TweetListItemHolder(View itemView) {
        super(itemView);

        userName= (TextView) itemView.findViewById(R.id.user_name);
        tweetInfo= (TextView) itemView.findViewById(R.id.tweet_text);
        tweetTime= (TextView) itemView.findViewById(R.id.time_stamp);
        userProfilePic= (ImageView) itemView.findViewById(R.id.user_img);
        layout= (CardView) itemView.findViewById(R.id.tweetListItem);


    }
}
