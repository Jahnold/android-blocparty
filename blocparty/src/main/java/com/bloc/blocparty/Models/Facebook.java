package com.bloc.blocparty.Models;

import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  Class responsible for all Facebook Interactions
 */
public class Facebook extends Social {

    private Session mSession;

    public Facebook() {

        mSession = Session.getActiveSession();

    }

    @Override
    public void loadFeed(final FeedListener listner) {

        // create some parameters for the feed request
        Bundle params = new Bundle();
        params.putString("filter", "app_2305272732");       // only posts with photos
        params.putString("fields", "object_id,from,created_time,likes,picture");

        new Request(
                mSession,
                "/me/home",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {

                        ArrayList<SocialItem> items = new ArrayList<SocialItem>();

                        try {

                            // create a JSON array from the response
                            JSONObject json = new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = json.getJSONArray("data");

                            // loop through out JSON array of posts
                            for (int i = 0; i < jsonArray.length(); i++) {

                                // get the current post
                                JSONObject post = jsonArray.getJSONObject(i);

                                // 'from' is a sub-object of the post
                                JSONObject from = post.getJSONObject("from");

                                // create a new SocialItem to put the post details into
                                SocialItem socialItem = new SocialItem();

                                // set the network to [this] so that we have a ref to it
                                socialItem.setNetwork(Facebook.this);

                                // transfer the details from the json to the social item
                                socialItem.setUserName(from.getString("name"));
                                socialItem.setUserId(from.getString("id"));
                                socialItem.setImageLink("https://graph.facebook.com/" + post.getString("object_id") + "/picture");
                                socialItem.setUniqueId(post.getString("object_id"));

                                // work out whether the logged in user has liked this post


                                // add our social item to the array list
                                items.add(socialItem);

                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }

                        // pass the array list of social items to the calling fragment via the listener
                        listner.onFeedLoaded(items);

                    }

                }
        ).executeAsync();

    }

    @Override
    public boolean likeItem(SocialItem item) {
        return false;
    }


}
