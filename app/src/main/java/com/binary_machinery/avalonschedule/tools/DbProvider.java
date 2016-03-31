/*
 * Copyright 2016 Evgeny Prikhodko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binary_machinery.avalonschedule.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class DbProvider extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "schedule_db";

    public static class Metadata {
        public static final String TABLE_NAME = "metadata";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_URL = "url";
        public static final int ID = 1;
    }

    public static class Records {
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_ROW_ID = "_id";
        public static final String COLUMN_RECORD_ID = "record_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEEKDAY = "weekday";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COURSE = "course";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_LECTURER = "lecturer";
        public static final String COLUMN_RECORD_TYPE = "record_type";

        public enum Type {
            Schedule,
            Added,
            Deleted
        }
    }

    public DbProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("CREATE TABLE ").append(Records.TABLE_NAME).append(" (")
                    .append(Records.COLUMN_ROW_ID).append(" INTEGER, ")
                    .append(Records.COLUMN_RECORD_ID).append(" INTEGER, ")
                    .append(Records.COLUMN_DATE).append(" INTEGER, ")
                    .append(Records.COLUMN_WEEKDAY).append(" TEXT, ")
                    .append(Records.COLUMN_TIME).append(" TEXT, ")
                    .append(Records.COLUMN_TYPE).append(" TEXT, ")
                    .append(Records.COLUMN_COURSE).append(" TEXT, ")
                    .append(Records.COLUMN_ROOM).append(" TEXT, ")
                    .append(Records.COLUMN_LECTURER).append(" TEXT, ")
                    .append(Records.COLUMN_RECORD_TYPE).append(" INTEGER, ")
                    .append("PRIMARY KEY (")
                    .append(Records.COLUMN_ROW_ID).append(", ")
                    .append(Records.COLUMN_RECORD_TYPE)
                    .append(")")
                    .append(");");
            db.execSQL(queryBuilder.toString());
        }
        {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("CREATE TABLE ").append(Metadata.TABLE_NAME).append(" (")
                    .append(Metadata.COLUMN_ID).append(" INTEGER PRIMARY KEY, ")
                    .append(Metadata.COLUMN_URL).append(" TEXT")
                    .append(");");
            db.execSQL(queryBuilder.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
