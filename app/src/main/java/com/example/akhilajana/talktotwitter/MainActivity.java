package com.example.akhilajana.talktotwitter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import twitter4j.Status;

public class MainActivity extends AppCompatActivity implements TwitterService.IData{

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String searchKeyword;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    ArrayList<Tweet> firebaseTweetList;

    private static HashSet<String> keywords = new HashSet<>();

    static {
        keywords.add(" about ");
        keywords.add(" on ");
        keywords.add(" for ");
        keywords.add(" from ");
        keywords.add(" by ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Talk to Twitter");

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef=database.getReference();
        getInputData();

        // hide the action bar
        //getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();

            /*if(firebaseTweetList == null || firebaseTweetList.size() == 0){
                manipulateInput(" by Donald Trump");
            } else {
                buildIntent(firebaseTweetList);
            }*/

        }
        });

    }

    /** Showing google speech input dialog * */
    private void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Receiving speech input * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
                {
                if (resultCode == RESULT_OK && null != data)
                {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("Yo","Result is: "+result);

                    txtSpeechInput.setText(result.get(0));

                    if(firebaseTweetList == null || firebaseTweetList.size() == 0){
                        manipulateInput(result.get(0));
                    } else {
                        buildIntent(firebaseTweetList);
                    }

                }
                break;
            }

        }
    }

    private void manipulateInput(String userInput) {
        boolean keywordExists = false;
        for(String keyword : keywords) {
            if(userInput.contains(keyword)) {
                int keywordIndex = userInput.indexOf(keyword);
                searchKeyword = userInput.substring(keywordIndex + keyword.length());
                keywordExists = true;

                makeTwitterCall(searchKeyword, Constants.THING_KEYWORD);

                break;
            }
        }

        if (!keywordExists) {
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show();
        }

    }

    private void getInputData() {
        dbRef.child("keywords").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseTweetList = new ArrayList<>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot entry : postSnapshot.getChildren()) {
                        Tweet tweet = entry.getValue(Tweet.class);
                        firebaseTweetList.add(tweet);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}



    private void makeTwitterCall(String input, String queryType){
        new TwitterService(MainActivity.this, MainActivity.this, queryType).execute(input);
    }

    @Override
    public void setupData(TweetsList tweetsList) {
        ArrayList<Tweet> tweets = tweetsList.getTweets();
        addTweetsToFirebase(tweets);
        buildIntent(tweets);
    }

    public void addTweetsToFirebase(ArrayList<Tweet> tweets){

        int index = 0;
        for(Tweet tweet : tweets) {
            dbRef.child("keywords").child(searchKeyword).child("tweet " + index).setValue(new Tweet(tweet.getCreatedAt(), tweet.getUser(), tweet.getTweetContent()));
            index++;
        }
    }

    private void buildIntent(ArrayList<Tweet> tweets) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TweetKey", tweets);
        intent.putExtra("BundleKey", bundle);
        startActivity(intent);
    }
}
