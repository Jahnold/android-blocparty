package com.bloc.blocparty.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Base class for the social media links
 */
public abstract class Social {

    protected String dateFormat;

    public interface FeedListener {

        void onFeedLoaded(ArrayList<SocialItem> items);
        void onFeedLoadFailure();

    }

    public interface LikeListener {

        void onLikeSuccess();

    }

    public abstract void loadFeed(FeedListener listner);

    public abstract void likeItem(SocialItem item, LikeListener listener);

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
