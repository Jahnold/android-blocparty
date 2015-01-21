package com.bloc.blocparty.Models;

import com.bloc.blocparty.Fragments.FeedFragment;

import java.util.ArrayList;

/**
 * Created by matthewarnold on 16/01/15.
 */
public abstract class Social {

    public interface FeedListener {

        void onComplete(ArrayList<SocialItem> items);
        void onFailure();

    }

    public abstract void loadFeed(FeedListener listner);

    public abstract boolean likeItem(SocialItem item);

}
