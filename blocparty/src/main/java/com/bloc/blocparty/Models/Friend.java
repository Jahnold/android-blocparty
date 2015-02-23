package com.bloc.blocparty.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

/**
 *  Friend Model
 */
public class Friend extends Model {

    private static final int FACEBOOK = 0;
    private static final int TWITTER = 1;
    private static final int INSTAGRAM = 2;

    private String mName;
    private String mUniqueId;
    private int mNetwork;
    private Long mCollectionId;

    public Friend() {

        mTableName = "Friends";
    }

    // getters and setters

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getUniqueId() {
        return mUniqueId;
    }
    public void setUniqueId(String uniqueId) {
        mUniqueId = uniqueId;
    }
    public int getNetwork() {
        return mNetwork;
    }
    public void setNetwork(int network) {
        mNetwork = network;
    }
    public Long getCollectionId() {
        return mCollectionId;
    }
    public void setCollectionId(Long collectionId) {
        mCollectionId = collectionId;
    }

    @Override
    protected void _load(Cursor row) {

        // load the name from the cursor
        mName = row.getString(row.getColumnIndex("name"));
        mUniqueId = row.getString(row.getColumnIndex("unique_id"));
        mNetwork = row.getInt(row.getColumnIndex("network"));
        mCollectionId = row.getLong(row.getColumnIndex("collection_id"));

    }

    @Override
    protected ContentValues _getContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", mName);
        values.put("unique_id", mUniqueId);
        values.put("network", mNetwork);
        values.put("collection_id", mCollectionId);
        return values;
    }


}
