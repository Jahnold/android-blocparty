package com.bloc.blocparty.Models;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *  Handle all twitter interactions
 */
public class Twitter extends Social {

    @Override
    public void loadFeed(final FeedListener listener) {

        // get a ref to the twitter api service
        StatusesService service = com.twitter.sdk.android.Twitter.getApiClient().getStatusesService();

        service.homeTimeline(
                30,                     // tweet count
                null,                   // since_id
                null,                   // max_id
                false,                  // trim user
                true,                   // exclude replies
                true,                   // contributor details
                true,                   // include entities
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {

                        ArrayList<SocialItem> items = new ArrayList<SocialItem>();

                        Iterator iterator = listResult.data.iterator();
                        while (iterator.hasNext()) {

                            Tweet tweet = (Tweet) iterator.next();

                            // we're only interested in tweets with 'media
                            if (tweet.entities.media != null) {

                                // create a new social item from the tweet
                                SocialItem socialItem = new SocialItem(
                                        tweet.idStr,
                                        String.valueOf(tweet.user.id),
                                        tweet.user.name,
                                        tweet.text,
                                        convertDate(tweet.createdAt),
                                        tweet.favorited,
                                        tweet.user.profileImageUrl,
                                        tweet.entities.media.get(0).mediaUrl,
                                        Twitter.this
                                );

                                // add this to the array list
                                items.add(socialItem);

                            }

                        }

                        listener.onFeedLoaded(items);

                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                }
        );


    }

    @Override
    public boolean likeItem(SocialItem item) {
        return false;
    }

    private Date convertDate(String dateString) {

        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";

        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        Date returnDate = null;
        try {
            returnDate = sf.parse(dateString);
        }
        catch (Exception e) { e.printStackTrace();}

        return returnDate;
    }
}
