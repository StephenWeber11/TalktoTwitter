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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import twitter4j.Status;

public class MainActivity extends AppCompatActivity implements TwitterService.IData{

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String searchKeyword = "";

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

    private ArrayList<Keyword> tweetKeywords;

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
                firebaseTweetList = null;
                promptSpeechInput();
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

                    manipulateInput(result.get(0));

                    if(tweetKeywords != null) {
                        for (Keyword key : tweetKeywords) {
                            if (key.getKeyword().equals(searchKeyword)) {
                                firebaseTweetList = key.getTweets();
                                break;
                            }
                        }
                    }

                    if(firebaseTweetList == null || firebaseTweetList.size() == 0) {
                        makeTwitterCall(searchKeyword, Constants.THING_KEYWORD);
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

                break;
            }
        }

        if (!keywordExists) {
            searchKeyword = userInput;
        }

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
        dbRef.child("keywords").push().setValue(new Keyword(searchKeyword, tweets));
    }

    private void buildIntent(ArrayList<Tweet> tweets) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TweetKey", tweets);
        intent.putExtra("BundleKey", bundle);
        startActivity(intent);
    }

    private void getInputData() {
        dbRef.child("keywords").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseTweetList = new ArrayList<>();
                tweetKeywords = new ArrayList<>();

                Calendar cal = Calendar.getInstance();
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                String dayOfMonthStr = " " + String.valueOf(dayOfMonth) + " ";

                for (DataSnapshot entry : dataSnapshot.getChildren()) {
                    Keyword tweetKeyword = entry.getValue(Keyword.class);

                    boolean isOutOfDate = false;
                    for(Tweet tweet : tweetKeyword.getTweets()) {
                        if(!tweet.getCreatedAt().contains(dayOfMonthStr)) {
                            isOutOfDate = true;
                        }
                    }

                    if(!isOutOfDate) {
                        tweetKeywords.add(tweetKeyword);
                    } else {
                        dbRef.child("keywords").child(entry.getKey()).removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
