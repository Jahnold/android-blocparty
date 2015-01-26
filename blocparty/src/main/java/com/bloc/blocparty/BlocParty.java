package com.bloc.blocparty;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.bloc.blocparty.Fragments.AccountsFragment;
import com.bloc.blocparty.Fragments.FeedFragment;
import com.facebook.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.*;
import twitter4j.auth.AccessToken;


public class BlocParty extends Activity {

    // keep track of the resumed state of the app
    private boolean isResumed = false;

    private static final String PREF_NAME = "";


    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception e) {

            onSessionStateChange(session,state,e);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // For Facebook
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bloc_party);

        // For Twitter
        checkTwitterCallback();

        // set the feed fragment as the default view
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new FeedFragment())
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bloc_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_accounts:

                // load the account fragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AccountsFragment())
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_settings:

                // do nothing for now
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }


    }

    /**
     *  Called by the FB SDK callback when the session state has changed (login/logout)
     *
     */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

    }

    private void checkTwitterCallback() {

        Uri uri = getIntent().getData();
        String verifier = "";
        if (uri != null && uri.toString().startsWith(getString(R.string.twitter_callback_url))) {

            verifier = uri.getQueryParameter("oauth_verifier");

        }

        new AsyncTask<String,Void,AccessToken>() {

            @Override
            protected AccessToken doInBackground(String... params) {

                String verifier = params[0];
                Twitter twitter = TwitterFactory.getSingleton();
                AccessToken accessToken = null;

                try {

                    RequestToken requestToken = twitter.getOAuthRequestToken(getString(R.string.twitter_callback_url));

                    if (verifier != null && !verifier.equals("")) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                    }

                }
                catch (TwitterException e) { e.printStackTrace(); }


                return accessToken;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {

                super.onPostExecute(accessToken);
            }
        }.execute(verifier);


    }



}
