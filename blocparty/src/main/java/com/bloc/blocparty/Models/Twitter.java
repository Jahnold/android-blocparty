package com.bloc.blocparty.Models;

/**
 * Created by matthewarnold on 16/01/15.
 */
public class Twitter extends Social {

    @Override
    public SocialItem[] getFeed() {

        return new SocialItem[] {};
    }

    @Override
    public boolean likeItem(SocialItem item) {
        return false;
    }
}
