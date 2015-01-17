package com.bloc.blocparty.Models;

import com.facebook.Session;

/**
 *  Class responsible for all Facebook Interactions
 */
public class Facebook extends Social {

    @Override
    public SocialItem[] getFeed() {



        return new SocialItem[] {};

    }

    @Override
    public boolean likeItem(SocialItem item) {
        return false;
    }


}
