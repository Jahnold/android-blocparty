package com.bloc.blocparty.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bloc.blocparty.BlocPartyApplication;

import java.util.ArrayList;

/**
 *  Collection Model
 */
public class Collection extends Model {

    private String mName;
    private ArrayList<Friend> mFriends = new ArrayList<>();
    private Boolean friendsLoaded = false;

    // getus and setus
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public ArrayList<Friend> getFriends() {

        if (!friendsLoaded) {
            loadFriends();
        }

        return mFriends;
    }

    @Override
    protected void _load(Cursor row) {

        // load the name from the cursor
        mName = row.getString(row.getColumnIndex("name"));

    }

    @Override
    protected ContentValues _getContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", mName);
        return values;
    }

    /**
     *  Loads all the friends associated with this collection
     *
     */
    public void loadFriends() {

        SQLiteDatabase db = BlocPartyApplication.getDbHelper().getWritableDatabase();

        // query to get all friends with this collection id
        Cursor cursor = db.query(
                "Friends",
                new String[] {"*"},
                "collection_id = ?",
                new String[] {String.valueOf(mId)},
                null,null,null
        );

        // get the column ints
        int idColumn = cursor.getColumnIndex("_id");
        int nameColumn = cursor.getColumnIndex("_id");
        int uniqueIdColumn = cursor.getColumnIndex("_id");
        int networkColumn = cursor.getColumnIndex("network");

        while (cursor.moveToNext()) {

            // create a new friend object
            Friend friend = new Friend();
            friend.setId(cursor.getLong(idColumn));
            friend.setName(cursor.getString(nameColumn));
            friend.setUniqueId(cursor.getString(uniqueIdColumn));
            friend.setNetwork(cursor.getInt(networkColumn));
            friend.setCollectionId(mId);

            // add it to the array list
            mFriends.add(friend);
        }

        friendsLoaded = true;

    }

}
