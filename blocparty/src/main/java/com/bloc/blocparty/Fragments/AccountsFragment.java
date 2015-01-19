package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloc.blocparty.Models.Facebook;
import com.bloc.blocparty.R;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

/**
 * Fragment which handles the logging in to any/all accounts
 */
public class AccountsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        // get a ref to the FB button
        LoginButton fb = (LoginButton) view.findViewById(R.id.btn_login_facebook);

        // set the list of permissions that we want from FB
        fb.setReadPermissions(Arrays.asList("user_likes", "user_status", "read_stream"));

        // set up the fetch feed test
        Button btnFeed = (Button) view.findViewById(R.id.fetch_feed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Facebook facebook = new Facebook(getActivity());
                facebook.loadFeed();
            }
        });

        return view;

    }


}
