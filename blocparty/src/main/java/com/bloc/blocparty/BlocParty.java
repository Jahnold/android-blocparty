package com.bloc.blocparty;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bloc.blocparty.Fragments.AccountsFragment;
import com.bloc.blocparty.Fragments.FeedFragment;
import com.facebook.*;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;



public class BlocParty extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "AQmWwEByzqiVLCPQD7uGdCzft";
    private static final String TWITTER_SECRET = "TAEh29REDU8NUMoaM8bicT0CBKfPIuXQkTqqf9RUaVjaJmYmyg";

    // keep track of the resumed state of the app
    private boolean isResumed = false;


    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception e) {

            onSessionStateChange(session,state,e);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        // For Facebook
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bloc_party);

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

        //fb
        uiHelper.onActivityResult(requestCode, resultCode, data);

        //twitter
        Fragment accountFragement = getFragmentManager().findFragmentByTag("AccountsFragment");
        if (accountFragement != null) {
            accountFragement.onActivityResult(requestCode, resultCode, data);
        }


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
                        .replace(R.id.container, new AccountsFragment(),"AccountsFragment")
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





}
