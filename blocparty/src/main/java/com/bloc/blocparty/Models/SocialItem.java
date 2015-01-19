package com.bloc.blocparty.Models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 *  SocialItem Class is the container for all images and their associated social network content
 */
public class SocialItem {


    private Bitmap mImage;
    private String mImageLink;
    private String mUserName;
    private String mComment;
    private boolean mLike;
    private String mUserId;
    private Date mTimestamp;
    private enum mNetwork {FACEBOOK, TWITTER, INSTAGRAM}

    // getters & setters
    public String getImageLink() { return mImageLink; }
    public void setImageLink(String imageLink) { this.mImageLink = imageLink;}

    public Bitmap getImage() { return mImage; }
    public void setImage(Bitmap mImage) { this.mImage = mImage; }

    public String getUserName() { return mUserName; }
    public void setUserName(String mUserName) { this.mUserName = mUserName; }

    public String getComment() { return mComment; }
    public void setComment(String mComment) {  this.mComment = mComment; }

    public boolean isLike() { return mLike; }

    public String getUserId() { return mUserId; }
    public void setUserId(String mUserId) { this.mUserId = mUserId; }

    public Date getTimestamp() { return mTimestamp; }
    public void setTimestamp(Date mTimestamp) { this.mTimestamp = mTimestamp; }


    public void toggleLike() {



    }

}
