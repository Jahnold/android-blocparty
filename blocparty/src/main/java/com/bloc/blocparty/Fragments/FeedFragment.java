package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.blocparty.Adapters.FeedItemAdapter;
import com.bloc.blocparty.Models.Facebook;
import com.bloc.blocparty.Models.Social;
import com.bloc.blocparty.Models.SocialItem;
import com.bloc.blocparty.R;

import java.util.ArrayList;

/**
 * The Fragment which displays the feed
 */
public class FeedFragment extends Fragment{

    private ArrayList<SocialItem> mFeed;
    private FeedItemAdapter mAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_feed, container, false);

        // get ref to the list view
        ListView feedListView = (ListView) v.findViewById(R.id.feed_list);

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

    public void loadFeed() {

        Facebook fb = new Facebook();
        fb.loadFeed(new Social.FeedListener() {
            @Override
            public void onComplete(ArrayList<SocialItem> items) {

                // add the items to the feed and update the adapter
                addToFeed(items);
                notifyUpdate();

            }

            @Override
            public void onFailure() {
                // do nothing as yet
            }
        });

    }

    public void clearFeed() {

        mFeed.clear();

    }

    public void addToFeed(ArrayList<SocialItem> newItems) {

        mFeed.addAll(newItems);

    }

    public void notifyUpdate() {

        mAdapter.notifyDataSetChanged();

    }
}
