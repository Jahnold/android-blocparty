package com.bloc.blocparty.Models;

/**
 * Created by matthewarnold on 16/01/15.
 */
public abstract class Social {

    public abstract void loadFeed();

    public abstract boolean likeItem(SocialItem item);

}
