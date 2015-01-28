package com.bloc.blocparty.Models;

import com.bloc.blocparty.Fragments.FeedFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by matthewarnold on 16/01/15.
 */
public abstract class Social {

    protected String dateFormat;

    public interface FeedListener {

        void onFeedLoaded(ArrayList<SocialItem> items);
        void onFeedLoadFailure();

    }

    public abstract void loadFeed(FeedListener listner);

    public abstract boolean likeItem(SocialItem item);

    protected final Date convertDate(String dateString) {

        SimpleDateFormat sf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sf.setLenient(true);
        Date returnDate = null;
        try {
            returnDate = sf.parse(dateString);
        }
        catch (Exception e) { e.printStackTrace();}

        return returnDate;
    }

}
