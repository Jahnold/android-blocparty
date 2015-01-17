package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        fb.setReadPermissions(Arrays.asList("user_likes", "user_status"));

        return view;

    }


}
