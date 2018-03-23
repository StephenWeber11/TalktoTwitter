package com.example.akhilajana.talktotwitter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;

    ArrayList<String> inputList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef=database.getReference();

        inputList = new ArrayList<>();

        // hide the action bar
        //getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

                }
                break;
            }

        }
    }

    private void manipulateInput(String userInput) {

        //show me tweets about, get me tweets on, show me tweets from
        if (userInput.startsWith("show me tweets about"))
        {
            //remove the first 20 characters
            String mod_Input = userInput.substring(21);

            dbRef.child(mod_Input).setValue(mod_Input);

        }
        else if (userInput.startsWith("get me tweets on"))
        {
            //remove the first 20 characters
            String mod_Input = userInput.substring(17);
            dbRef.child(mod_Input).setValue(mod_Input);

        }

        else if (userInput.startsWith("show me tweets from"))
        {
            //remove the first 20 characters
            String mod_Input = userInput.substring(20);
            dbRef.child(mod_Input).setValue(mod_Input);

        }
        else
        {
            Toast.makeText(this, "Make sure your query starts with Show me tweets about abcc", Toast.LENGTH_LONG).show();
        }


    }

    private void getInputData()
    {
        Query query=dbRef.child("User_Input");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String inputObj=dataSnapshot.getValue(String.class);
                inputList.add(inputObj);
                Log.d("listSize",inputList.size()+"");

                //Replace 1st element if list size exceeds 3
                if(inputList.size()>3)
                {
                    inputList.remove(0);
                }

                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
