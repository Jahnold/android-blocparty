package com.bloc.blocparty.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.bloc.blocparty.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *  Handle all twitter interactions
 */
public class Twitter extends Social {

    private Context mContext;

    public Twitter(Context context) {

        dateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        mContext = context;

    }

    @Override
    public void loadFeed(final FeedListener listener) {

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
    public void likeItem(SocialItem item, final LikeListener listener) {


        FavoriteService service =  com.twitter.sdk.android.Twitter.getApiClient().getFavoriteService();

        service.create(
                Long.valueOf(item.getUniqueId()),
                false,
                new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> tweetResult) {

                        listener.onLikeSuccess();

                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                }
        );

    }

    public void postPhoto(Bitmap image, String comment) {


        // transfer details from SDK to Twitter4J
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mContext.getString(R.string.twitter_key))
               .setOAuthConsumerSecret(mContext.getString(R.string.twitter_secret))
               .setOAuthAccessToken(com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession().getAuthToken().token)
               .setOAuthAccessTokenSecret(com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession().getAuthToken().secret);

        Configuration config = builder.build();
        TwitterFactory factory = new TwitterFactory(config);
        twitter4j.Twitter twitter = factory.getInstance();

        // get a File from our image
        String filePath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),image,null,null);
        File imageFile = new File(filePath);

        try {

            StatusUpdate tweet = new StatusUpdate(comment);
            tweet.setMedia(imageFile);
            twitter.updateStatus(tweet);

        }
        catch (twitter4j.TwitterException e) { e.printStackTrace(); }

    }



}
