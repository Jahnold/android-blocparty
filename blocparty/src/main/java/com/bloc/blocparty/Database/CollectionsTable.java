package com.bloc.blocparty.Database;

/**
 * Created by matthewarnold on 12/02/15.
 */
public class CollectionsTable extends Table {

    public NotesTable(String name) {

        super(name);

    }

    @Override
    public String getCreateStatement() {

        String q = "CREATE TABLE Notes ( " +
                "    _id INTEGER PRIMARY KEY, " +
                "    text TEXT, " +
                "    notebook_id INTEGER " +
                ")";
        return q;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int oldVersion, int NewVersion) {

        // for version 2 add the 'image_url' field to the notes table
        if (oldVersion == 1 & NewVersion == 2) {

            sqlLiteDatabase.execSQL(
                    "ALTER TABLE Notes " +
                            "ADD COLUMN image_url TEXT"
            );

        }

    }
}
