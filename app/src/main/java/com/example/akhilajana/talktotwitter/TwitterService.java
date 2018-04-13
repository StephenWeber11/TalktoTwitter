package com.example.akhilajana.talktotwitter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
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
    private List<twitter4j.Status> mTweets;
    private String queryType;

    public TwitterService(Activity activity, IData iDataActivty, String queryType) {
        this.activity = activity;
        this.iDataActivty = iDataActivty;
        this.queryType = queryType;
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
            query.setLang("en");
            query.setCount(10);

            QueryResult result;
            if(queryType.equals(Constants.PERSON_KEYWORD)) {
                query.setQuery("source: " + params[0]);
                result = twitter.search(query);
                mTweets = result.getTweets();

            } else {
                query.setQuery(params[0]);
                result = twitter.search(query);
                mTweets = result.getTweets();


                Log.d("Twitter", "Twitter Result: " + result);
            }

            Log.d("Twitter", "TweetsList Size: " + mTweets.size());

            return mTweets;

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
            TweetsList tweetsList = new TweetsList();
            tweetsList.setTweets(mapResult(mTweets));
            iDataActivty.setupData(tweetsList);
        }
    }

    public interface IData {
        void setupData(TweetsList tweetsList);
    }

    protected ArrayList<Tweet> mapResult(List<twitter4j.Status> result) {
        ArrayList<Tweet> tweets = new ArrayList<>(result.size());
        for(twitter4j.Status status : result) {
            Tweet tweet = new Tweet();
            tweet.setCreatedAt(status.getCreatedAt().toString());
            tweet.setTweetContent(status.getText());

            User user = new User();
            user.setLocation(status.getUser().getLocation());
            user.setName(status.getUser().getName());
            user.setImageUrl(status.getUser().getProfileImageURL());

            tweet.setUser(user);
            tweets.add(tweet);
        }

        return tweets;
    }
}
