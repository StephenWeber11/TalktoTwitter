package com.example.akhilajana.talktotwitter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by StephenWeber on 4/3/2018.
 */

public class TwitterService extends AsyncTask<String, Void, List<Status>> {

    private Twitter twitter;
    private Activity activity;
    private IData iDataActivty;
    private List<twitter4j.Status> tweets;

    public TwitterService(Activity activity, IData iDataActivty) {
        this.activity = activity;
        this.iDataActivty = iDataActivty;
    }

    @Override
    protected List<twitter4j.Status> doInBackground(String... params) {
        try {

            if (twitter == null) {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(false)
                        .setOAuthConsumerKey("gISje5cctQNKQi4WMHZMaEt7R")
                        .setOAuthConsumerSecret("kKkpE68ZHnTu0URE0VKtSRDEIsGQXlJLfAUb83T393Dw6HY72H")
                        .setApplicationOnlyAuthEnabled(true)
                        .setHttpConnectionTimeout(5000)
                        .setHttpReadTimeout(5000)
                        .setHttpStreamingReadTimeout(5000);
                twitter = new TwitterFactory(cb.build()).getInstance();
                twitter.getOAuth2Token();
            }

            Query query = new Query();
            query.setQuery(params[0]);
            query.setLang("en");
            query.setCount(10);

            QueryResult result = twitter.search(query);
            tweets = result.getTweets();

            Log.d("Twitter", "Twitter Result: " + result);
            Log.d("Twitter", "TweetsList Size: " + tweets.size());


            return tweets;

            /*
            --header 'authorization: OAuth oauth_consumer_key="consumer-key-for-app",
            oauth_nonce="generated-nonce", oauth_signature="generated-signature",
            oauth_signature_method="HMAC-SHA1", oauth_timestamp="generated-timestamp",
            oauth_token="access-token-for-authed-user", oauth_version="1.0"'
            */

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> result) {
        if(result == null || result.size() == 0) {
            Toast.makeText(activity, "No Tweets Found", Toast.LENGTH_SHORT).show();
        } else {
            iDataActivty.setupData(tweets);
        }
    }

    static public interface IData {
        public void setupData(List<twitter4j.Status> result);
    }
}
