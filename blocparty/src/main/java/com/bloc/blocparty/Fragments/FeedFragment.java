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
import com.bloc.blocparty.Models.Collection;
import com.bloc.blocparty.Models.Facebook;
import com.bloc.blocparty.Models.Friend;
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

    public static int LOAD_ALL = 0;
    public static int LOAD_COLLECTION = 1;

    private int mFeedType = LOAD_ALL;
    private ArrayList<SocialItem> mFeed = new ArrayList<>();
    private FeedItemAdapter mAdapter;
    private ListView mFeedListView;
    private Collection mCollection;


    public FeedFragment() {}

    // setter
    public void setCollection(Collection collection) {
        mCollection = collection;
    }
    public void setFeedType(int feedType) { mFeedType = feedType; }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_feed, container, false);

        // get ref to the list view
        mFeedListView = (ListView) v.findViewById(R.id.feed_list);

        // either load all or load the collection
        if (mFeedType == LOAD_ALL) {
            loadAll();
        }
        else {
            loadCollection();
        }

        // set up the feed item adapter
        mAdapter = new FeedItemAdapter(
                getActivity(),
                0,
                mFeed
        );

        // introduce the list view to the adapter
        mFeedListView.setAdapter(mAdapter);

        // return the view
        return v;

    }

    /**
     *  Loads all latest items from all logged in networks
     */
    private void loadAll() {

        // get facebook
        Session fbSession = Session.getActiveSession();

        final FeedFragment feedFragment = this;

        // add a callback so that when the session is opened we load the feed
        fbSession.addCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception e) {
                if (state.isOpened()) {

                    Facebook fb = new Facebook(getActivity());
                    fb.loadFeed(feedFragment);

                }
            }
        });

        // get twitter
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();

        // if we have an open twitter session then load the feed
        if (twitterSession != null) {

            com.bloc.blocparty.Models.Twitter twitter = new com.bloc.blocparty.Models.Twitter(getActivity());
            twitter.loadFeed(this);

        }

        // get instagram
        InstagramSession instagramSession = new InstagramSession(getActivity());

        // if we have an active instagram session, load the feed
        if (instagramSession.getAccessToken() != null) {

            Instagram instagram = new Instagram(getActivity());
            instagram.loadFeed(this);

        }
    }

    /**
    *   Loads all the latest items from the users in the collection
    *
    */
    private void loadCollection() {

        Facebook fb = new Facebook(getActivity());
        com.bloc.blocparty.Models.Twitter twitter = new com.bloc.blocparty.Models.Twitter(getActivity());
        Instagram instagram = new Instagram(getActivity());

        // loop through all the friends in the collection and load the feed from the relevant network
        for (Friend friend : mCollection.getFriends()) {

            switch (friend.getNetwork()) {

                case Friend.FACEBOOK:
                    fb.loadUserFeed(friend.getUniqueId(), this);
                    break;
                case Friend.TWITTER:
                    twitter.loadUserFeed(friend.getUniqueId(), this);
                    break;
                case Friend.INSTAGRAM:
                    instagram.loadUserFeed(friend.getUniqueId(), this);

            }
        }

    }

    public void clearFeed() {

        mFeed.clear();

    }

    public void addToFeed(ArrayList<SocialItem> newItems) {

        mAdapter.addAll(newItems);

    }

    public void notifyUpdate() {

        mAdapter.notifyDataSetChanged();

    }

    public void onFeedLoaded(ArrayList<SocialItem> items) {

        // add the items to the feed and update the adapter
        addToFeed(items);

    }

    public void onFeedLoadFailure() {

    }
}
