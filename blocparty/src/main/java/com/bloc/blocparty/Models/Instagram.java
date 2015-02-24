package com.bloc.blocparty.Models;

import android.content.Context;
import android.os.AsyncTask;

import com.bloc.blocparty.Instagram.InstagramSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Handle Instagram Interaction
 */
public class Instagram extends Social {

    private InstagramSession mSession;

    public Instagram(Context context) {

        // set the date format
        dateFormat = "";

        mSession = new InstagramSession(context);

    }

    @Override
    public void loadFeed(final FeedListener listener) {

        if (mSession.getAccessToken() != null) {

            new AsyncTask<Void,Void,String>() {

                @Override
                protected String doInBackground(Void... params) {

                    String response = "";

                    try {

                        URL url = new URL("https://api.instagram.com/v1/users/self/feed?access_token=" + mSession.getAccessToken());
                        InputStream inputStream = url.openConnection().getInputStream();
                        response = streamToString(inputStream);

                    }
                    catch (IOException e) { e.printStackTrace(); }

                    return response;

                }

                @Override
                protected void onPostExecute(String response) {

                    ArrayList<SocialItem> items = new ArrayList<>();

                    try {

                        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject post = jsonArray.getJSONObject(i);
                            JSONObject user = post.getJSONObject("user");

                            // create a new social item to put the post details in
                            SocialItem item = new SocialItem(
                                    post.getString("id"),
                                    user.getString("id"),
                                    user.getString("full_name"),
                                    post.getJSONObject("caption").getString("text"),
                                    new Date(Long.valueOf(post.getString("created_time"))*1000),
                                    post.getBoolean("user_has_liked"),
                                    user.getString("profile_picture"),
                                    post.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
                                    Instagram.this
                            );

                            // add the item to the list
                            items.add(item);
                        }

                    }
                    catch (JSONException e) { e.printStackTrace();}

                    // pass the items list back to the listener
                    listener.onFeedLoaded(items);

                }

            }.execute();

        }

    }

    /**
     *  Loads the feed items of a single user
     *
     */
    public void loadUserFeed(final String user, final FeedListener listener) {

        if (mSession.getAccessToken() != null) {

            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {

                    String response = "";

                    try {

                        URL url = new URL("https://api.instagram.com/v1/users/" + user + "/media/recent/?access_token=" + mSession.getAccessToken());
                        InputStream inputStream = url.openConnection().getInputStream();
                        response = streamToString(inputStream);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return response;

                }

                @Override
                protected void onPostExecute(String response) {

                    ArrayList<SocialItem> items = new ArrayList<>();

                    try {

                        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject post = jsonArray.getJSONObject(i);
                            JSONObject user = post.getJSONObject("user");

                            // create a new social item to put the post details in
                            SocialItem item = new SocialItem(
                                    post.getString("id"),
                                    user.getString("id"),
                                    user.getString("full_name"),
                                    post.getJSONObject("caption").getString("text"),
                                    new Date(Long.valueOf(post.getString("created_time")) * 1000),
                                    post.getBoolean("user_has_liked"),
                                    user.getString("profile_picture"),
                                    post.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
                                    Instagram.this
                            );

                            // add the item to the list
                            items.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // pass the items list back to the listener
                    listener.onFeedLoaded(items);

                }

            }.execute();

        }

    }

    @Override
    public void likeItem(final SocialItem item, final LikeListener listener) {

        if (mSession.getAccessToken() != null) {

            new AsyncTask<Void,Void,String>() {

                @Override
                protected String doInBackground(Void... params) {

                    String response = "";

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://api.instagram.com/v1/media/" + item.getUniqueId() + "/likes");

                    try {

                        // create a nvp for the access token
                        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                        nameValuePairs.add(new BasicNameValuePair("access_token", mSession.getAccessToken()));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // get a response
                        HttpResponse httpResponse = httpclient.execute(httppost);
                        response = String.valueOf(httpResponse.getStatusLine().getStatusCode());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return response;

                }


                @Override
                protected void onPostExecute(String response) {

                    if (response.equals("200")) {

                        listener.onLikeSuccess();

                    }

                }

            }.execute();

        }

    }

    // nabbed from the InstagramApp class
    // should probably keep it somewhere accessible to all
    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }
}
