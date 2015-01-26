package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloc.blocparty.R;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

/**
 * Fragment which handles the logging in to any/all accounts
 */
public class AccountsFragment extends Fragment {

    private static final String PREF_TWITTER_IS_LOGGED_IN = "com.bloc.blocparty.pref_twitter_logged_in";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        /*~~~~~~~~~~~~
        *
        *   Facebook
        *
        ~~~~~~~~~~~~~*/

        // get a ref to the FB button
        LoginButton fb = (LoginButton) view.findViewById(R.id.btn_login_facebook);

        // set the list of permissions that we want from FB
        fb.setReadPermissions(Arrays.asList("user_likes", "user_status", "read_stream"));

        /*~~~~~~~~~~~
        *
        *   Twitter
        *
        ~~~~~~~~~~~~*/

        // get a ref to the Twitter button
        Button twitter = (Button) view.findViewById(R.id.btn_login_twitter);

        // set a click listener
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // first check if we're already logged into twitter by checking the shared prefs
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                if (!sharedPreferences.getBoolean(PREF_TWITTER_IS_LOGGED_IN, false)) {

                    // not already logged in, run the twitter login async task
                    new TwitterAuthenticateTask().execute();

                }
                else {

                    // already logged in - log out

                }


            }
        });

        return view;

    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected RequestToken doInBackground(String... params) {

            Twitter twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(getString(R.string.twitter_key),getString(R.string.twitter_secret));

            RequestToken requestToken = null;

            try {
                requestToken = twitter.getOAuthRequestToken(getString(R.string.twitter_callback_url));
            }
            catch (TwitterException e) { e.printStackTrace(); }

            return requestToken;

        }

        @Override
        protected void onPostExecute(RequestToken requestToken) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
            startActivity(intent);

        }
    }


}
