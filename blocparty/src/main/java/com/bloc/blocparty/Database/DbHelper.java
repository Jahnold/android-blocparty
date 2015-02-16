package com.bloc.blocparty.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  DbHelper
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BlocParty";

    private static Set<Table> sTables = new HashSet<>();
    static {
        sTables.add(new CollectionsTable("Collections"));
    }


    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Iterator<Table> tables = sTables.iterator();
        while (tables.hasNext()){
            sqLiteDatabase.execSQL(tables.next().getCreateStatement());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Iterator<Table> tables = sTables.iterator();
        while (tables.hasNext()) {
            tables.next().onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        }
    }
}
