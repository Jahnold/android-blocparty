package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.blocparty.Adapters.FeedItemAdapter;
import com.bloc.blocparty.Instagram.InstagramSession;
import com.bloc.blocparty.Models.Facebook;
import com.bloc.blocparty.Models.Instagram;
import com.bloc.blocparty.Models.Social;
import com.bloc.blocparty.Models.SocialItem;
import com.bloc.blocparty.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;

/**
 * The Fragment which displays the feed
 */
public class FeedFragment extends Fragment implements Social.FeedListener{

    private ArrayList<SocialItem> mFeed = new ArrayList<>();
    private FeedItemAdapter mAdapter;

    private String TAG = "FeedFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_feed, container, false);

        // get ref to the list view
        ListView feedListView = (ListView) v.findViewById(R.id.feed_list);

        // get facebook
        Session fbSession = Session.getActiveSession();

        final FeedFragment feedFragment = this;

        // add a callback so that when the session is opened we load the feed
        fbSession.addCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception e) {
                if (state.isOpened()) {

                    Facebook fb = new Facebook();
                    fb.loadFeed(feedFragment);

                }
            }
        });


        // get twitter
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();

        // if we have an open twitter session then load the feed
        if (twitterSession != null) {

            com.bloc.blocparty.Models.Twitter twitter = new com.bloc.blocparty.Models.Twitter();
            twitter.loadFeed(this);

        }

        // get instagram
        InstagramSession instagramSession = new InstagramSession(getActivity());

        // if we have an active instagram session, load the feed
        if (instagramSession.getAccessToken() != null) {

            Instagram instagram = new Instagram(getActivity());
            instagram.loadFeed(this);

        }

        // set up the feed item adapter
        mAdapter = new FeedItemAdapter(
                getActivity(),
                0,
                mFeed
        );

        // introduce the list view to the adapter
        feedListView.setAdapter(mAdapter);

        // return the view
        return v;

    }


    public void clearFeed() {

        mFeed.clear();

    }

    public void addToFeed(ArrayList<SocialItem> newItems) {

        //mFeed.addAll(newItems);
        mAdapter.addAll(newItems);

    }

    public void notifyUpdate() {

        mAdapter.notifyDataSetChanged();

    }

    public void onFeedLoaded(ArrayList<SocialItem> items) {

        // add the items to the feed and update the adapter
        addToFeed(items);
        //notifyUpdate();

    }

    public void onFeedLoadFailure() {

    }
}
