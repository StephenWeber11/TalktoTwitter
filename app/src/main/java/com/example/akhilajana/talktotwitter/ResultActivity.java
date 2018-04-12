package com.example.akhilajana.talktotwitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ResultActivity extends AppCompatActivity
{

    RecyclerView tweetsListView;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tweetsListView = findViewById(R.id.tweetsList);

        adapter = new TweetsAdapter(null);
        tweetsListView.setAdapter(adapter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetsListView.setLayoutManager(horizontalLayoutManager);
        tweetsListView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();


    }
}
