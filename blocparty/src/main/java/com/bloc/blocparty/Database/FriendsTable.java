package com.bloc.blocparty.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Friends Table
 */
public class FriendsTable extends Table {

    public FriendsTable(String name) {

        super(name);

    }

    @Override
    public String getCreateStatement() {

        String q = "CREATE TABLE Friends ( " +
                "    _id INTEGER PRIMARY KEY, " +
                "    unique_id TEXT, " +
                "    name TEXT, " +
                "    network INTEGER, " +
                "    collection_id INTEGER " +
                ")";
        return q;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int oldVersion, int NewVersion) {}


}
