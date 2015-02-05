package com.bloc.blocparty.Models;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *  Class responsible for all Facebook Interactions
 */
public class Facebook extends Social {

    private Session mSession;
    private Context mContext;

    public Facebook(Context context) {

        // set the date format
        dateFormat = "yyyy-MM-dd'T'HH:mm:ss+SSSS";

        // get the facebook session
        mSession = Session.getActiveSession();

        // keep the context
        mContext = context;

    }

    @Override
    public void loadFeed(final FeedListener listener) {


        Bundle params = new Bundle();
        // pass in access token
        params.putString("access token", mSession.getAccessToken());
        // filter posts to only the ones with photos
        //params.putString("filter", "app_2305272732");
        // get the user id and the post details from home
        params.putString("fields", "id,home.filter(app_2305272732){object_id,from,created_time,likes,story}");

        new Request(
                mSession,
                "me/",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {

                        ArrayList<SocialItem> items = new ArrayList<SocialItem>();

                        try {

                            // create a JSON array from the response
                            JSONObject json = new JSONObject(response.getRawResponse());
                            JSONArray jsonArray = json.getJSONObject("home").getJSONArray("data");

                            // get the ID of the current user
                            String userId = json.getString("id");

                            // loop through out JSON array of posts
                            for (int i = 0; i < jsonArray.length(); i++) {

                                // get the current post
                                JSONObject post = jsonArray.getJSONObject(i);

                                // 'from' is a sub-object of the post
                                JSONObject from = post.getJSONObject("from");

                                // work out whether the logged in user has liked this post
                                // default to false
                                boolean isLiked = false;

                                // first check if there is even a likes entry
                                if (post.has("likes")) {

                                    JSONArray likes = post.getJSONObject("likes").getJSONArray("data");
                                    // loop through all the likes and see whether the user id matches the current user
                                    for (int j = 0; j < likes.length(); j++) {
                                        if (likes.getJSONObject(j).getString("id").equals(userId)) {
                                            isLiked = true;
                                        }
                                    }
                                }

                                // story is not always present so set a default and test for it
                                String story = "";
                                if (post.has("story")) {
                                    story = post.getString("story");
                                }

                                // create a new SocialItem to put the post details into
                                SocialItem socialItem = new SocialItem(
                                        post.getString("object_id"),
                                        from.getString("id"),
                                        from.getString("name"),
                                        story,
                                        convertDate(post.getString("created_time")),
                                        isLiked,
                                        "https://graph.facebook.com/" + from.getString("id") + "/picture?type=square",
                                        "https://graph.facebook.com/" + post.getString("object_id") + "/picture",
                                        Facebook.this
                                );

                                // add our social item to the array list
                                items.add(socialItem);

                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }

                        // pass the array list of social items to the calling fragment via the listener
                        listener.onFeedLoaded(items);

                    }

                }
        ).executeAsync();

    }

    @Override
    public void likeItem(SocialItem item, final LikeListener listener) {

        // make sure we've got publish permissions
        checkAndRequestPublishPermissions();

        new Request(
                mSession,
                // to do a like you nav to the post_id/likes
                item.getUniqueId() + "/likes",
                null,
                // POST for an insert
                HttpMethod.POST,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {

                        listener.onLikeSuccess();
                    }
                }

        ).executeAsync();

    }

    private void checkAndRequestPublishPermissions() {


        List<String> newPermissions = Arrays.asList("publish_actions", "publish_stream");
        List<String> currentPermissions = mSession.getPermissions();

        // check whether we have publish permissions
        if (!currentPermissions.containsAll(newPermissions)) {

            // seems not, lets get them
            Session.NewPermissionsRequest request = new Session.NewPermissionsRequest((Activity) mContext,newPermissions);
            mSession.requestNewPublishPermissions(request);

        }

    }

}
