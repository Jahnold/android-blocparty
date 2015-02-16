package com.bloc.blocparty.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bloc.blocparty.BlocPartyApplication;

import java.util.ArrayList;

/**
 *  Make collections here
 */
public class CollectionFactory {

    /**
     *  Return an array list of all the collections in the db
     *
     */
    public static ArrayList<Collection> getAll() {

        ArrayList<Collection> items = new ArrayList<>();

        // get a db ref
        SQLiteDatabase db = BlocPartyApplication.getDbHelper().getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Collections", null);

        // get the column numbers
        int idColumn = cursor.getColumnIndex("_id");
        int nameColumn = cursor.getColumnIndex("name");

        while (cursor.moveToNext()) {

            // create a new collection object
            Collection collection = new Collection();
            collection.setId(cursor.getLong(idColumn));
            collection.setName(cursor.getString(nameColumn));

            // add collection to the array list
            items.add(collection);
        }

        return  items;

    }
}
