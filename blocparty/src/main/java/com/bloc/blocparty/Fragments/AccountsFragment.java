package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloc.blocparty.Instagram.InstagramApp;
import com.bloc.blocparty.R;

import com.facebook.widget.LoginButton;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

/**
 * Fragment which handles the logging in to any/all accounts
 */
public class AccountsFragment extends Fragment {

    private static final String PREF_TWITTER_IS_LOGGED_IN = "com.bloc.blocparty.pref_twitter_logged_in";
    private TwitterLoginButton loginButton;

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

        loginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                //TODO
                // because the twitter button doesn't automatically change to 'log out'
                // I'll have to implement a change here


            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        /*~~~~~~~~~~~
        *
        *   Instagram
        *
        ~~~~~~~~~~~~*/

        final Button ig = (Button) view.findViewById(R.id.btn_login_instagram);

        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create a new instagram session
                InstagramApp instagram = new InstagramApp(
                        getActivity(),
                        getString(R.string.instagram_id),
                        getString(R.string.instagram_secret),
                        getString(R.string.instagram_uri)
                        );

                // set a listener for once authorisation is complete
                instagram.setListener(new InstagramApp.OAuthAuthenticationListener() {
                    @Override
                    public void onSuccess() {

                        // change the button to 'log out'
                        ig.setText(getString(R.string.instagram_btn_logout));

                    }

                    @Override
                    public void onFail(String error) {

                    }
                });

                // run the oAuth process
                instagram.authorize();

            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }




}
