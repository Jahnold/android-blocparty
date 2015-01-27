package com.bloc.blocparty.Models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 *  SocialItem Class is the container for all images and their associated social network content
 */
public class SocialItem {

    private String mUniqueId;
    private String mImageLink;
    private String mUserName;
    private String mUserId;
    private String mUserProfilePicLink;
    private String mComment;
    private boolean mLike;

    private Date mTimestamp;
    private Social mNetwork;

    public SocialItem() {}

    public SocialItem(String uniqueId, String userId, String userName, String comment, Date timestamp, boolean isLiked, String profilePicLink, String imageLink, Social network) {

        mUniqueId = uniqueId;
        mImageLink = imageLink;
        mUserId = userId;
        mUserName = userName;
        mUserProfilePicLink = profilePicLink;
        mComment = comment;
        mTimestamp = timestamp;
        mLike = isLiked;
        mNetwork = network;

    }

    // getters & setters
    public String getUniqueId() { return mUniqueId; }
    public void setUniqueId(String mUniqueId) { this.mUniqueId = mUniqueId; }

    public String getImageLink() { return mImageLink; }
    public void setImageLink(String imageLink) { this.mImageLink = imageLink;}

    public String getUserName() { return mUserName; }
    public void setUserName(String mUserName) { this.mUserName = mUserName; }

    public String getComment() { return mComment; }
    public void setComment(String mComment) {  this.mComment = mComment; }

    public boolean isLike() { return mLike; }

    public String getUserId() { return mUserId; }
    public void setUserId(String userId) {

        this.mUserId = userId;

        // we can also work out the profile pic link
        this.mUserProfilePicLink = "https://graph.facebook.com/" + userId + "/picture?type=square";

    }
    public String getUserProfilePicLink() { return mUserProfilePicLink; }

    public Date getTimestamp() { return mTimestamp; }
    public void setTimestamp(Date mTimestamp) { this.mTimestamp = mTimestamp; }

    public void setNetwork(Social network) { this.mNetwork = network; }
    public Social getNetwork() { return mNetwork; }

    public void toggleLike() {

        this.mLike = !this.mLike;

    }

}
