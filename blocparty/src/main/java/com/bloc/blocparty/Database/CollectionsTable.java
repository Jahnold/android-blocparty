package com.bloc.blocparty.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 *  Details of the collections table
 */
public class CollectionsTable extends Table {

    public CollectionsTable(String name) {

        super(name);

    }

    @Override
    public String getCreateStatement() {

        String q = "CREATE TABLE Collections ( " +
                "    _id INTEGER PRIMARY KEY, " +
                "    name TEXT " +
                ")";

        return q;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int oldVersion, int NewVersion) {}


}
