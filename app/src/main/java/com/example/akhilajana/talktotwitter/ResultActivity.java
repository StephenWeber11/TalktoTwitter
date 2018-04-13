package com.example.akhilajana.talktotwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity
{

    RecyclerView tweetsListView;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Talk to Twitter");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BundleKey");
        ArrayList<Tweet> tweets = (ArrayList<Tweet>) bundle.getSerializable("TweetKey");

        tweetsListView = findViewById(R.id.tweetsList);

        adapter = new TweetsAdapter(tweets, this);
        tweetsListView.setAdapter(adapter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetsListView.setLayoutManager(horizontalLayoutManager);
        tweetsListView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();


    }
}
