package com.bloc.blocparty;

import android.app.Application;

import com.bloc.blocparty.Database.DbHelper;

/**
 *  Singleton Manager
 */
public class BlocPartyApplication extends Application {

    private static DbHelper mDB;

    public BlocPartyApplication() {}

    @Override
    public void onCreate() {

        // set up out singleton db instance
        mDB = new DbHelper(getApplicationContext());
    }

    public static DbHelper getDbHelper() {

        return mDB;

    }
}
