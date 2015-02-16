package com.bloc.blocparty.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bloc.blocparty.BlocPartyApplication;

/**
 *  DB Model
 */
public abstract class Model {

    protected long mId;                 // the model id
    protected String mTableName;        // the name of the table

    // getters & setters
    public Long getId() {
        return mId;
    }
    public void setId(Long id) {
        mId = id;
    }

    /**
     *  Delete the entry which corresponds to this object from the db
     */
    public final void delete() {

        // first check we have a valid ID
        if (mId != 0) {

            SQLiteDatabase db = BlocPartyApplication.getDbHelper().getWritableDatabase();
            db.delete(mTableName, "_id = ?", new String[] {String.valueOf(mId)});

            db.close();
        }

    }

    /**
     *  Save the contents of the current object to the db
     *  If it already has an ID then do an update
     *  If not create a new entry
     */
    public final void save() {

        ContentValues values = _getContentValues();
        SQLiteDatabase db = BlocPartyApplication.getDbHelper().getWritableDatabase();

        // if the id == 0 then this is a new model
        // if not then it's an update
        if (mId == 0) {

            // new model, do insert and capture the new id
            mId = db.insert(
                    mTableName,
                    null,
                    values
            );

        }
        else {

            // updated
            db.update(
                    mTableName,
                    values,
                    "_id = ? ",
                    new String[] {String.valueOf(mId)}
            );
        }

        db.close();

    }

    protected abstract void _load(Cursor row);

    protected abstract ContentValues _getContentValues();
}
