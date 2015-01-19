package com.bloc.blocparty.Models;

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

import java.util.ArrayList;

/**
 *  Class responsible for all Facebook Interactions
 */
public class Facebook extends Social {

    private Session mSession;
    private Context mContext;

    public Facebook(Context context) {

        mSession = Session.getActiveSession();
        mContext = context;

    }

    @Override
    public void loadFeed() {

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

                        //gonna stick a breakpoint in here so I can see the response
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

                                // transfer the details from the json to the social item
                                socialItem.setUserName(from.getString("name"));
                                socialItem.setUserId(from.getString("id"));

                                // add our social item to the array list
                                items.add(socialItem);

                            }

                        }
                        catch (JSONException e) { e.printStackTrace(); }

                        // pass the array list of social items to the feed fragment

                    }

                }
        ).executeAsync();

    }

    @Override
    public boolean likeItem(SocialItem item) {
        return false;
    }


}
