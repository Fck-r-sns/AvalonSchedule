package com.binary_machinery.polytechschedule.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class DbProvider extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "schedule_db";
    public static final String TABLE_NAME = "schedule";

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_WEEKDAY = "weekday";
    public static final String KEY_TIME = "time";
    public static final String KEY_TYPE = "type";
    public static final String KEY_COURSE = "course";
    public static final String KEY_ROOM = "room";
    public static final String KEY_LECTURER = "lecturer";

    public DbProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
                .append(KEY_ID).append(" INTEGER PRIMARY KEY, ")
                .append(KEY_DATE).append(" TEXT, ")
                .append(KEY_WEEKDAY).append(" TEXT, ")
                .append(KEY_TIME).append(" TEXT, ")
                .append(KEY_TYPE).append(" TEXT, ")
                .append(KEY_COURSE).append(" TEXT, ")
                .append(KEY_ROOM).append(" TEXT, ")
                .append(KEY_LECTURER).append(" TEXT")
                .append(");");
        db.execSQL(queryBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
