package com.binary_machinery.polytechschedule.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.binary_machinery.polytechschedule.data.ScheduleRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class ScheduleStorager {
    private DbProvider m_dbProvider;

    public ScheduleStorager(DbProvider dbProvider) {
        m_dbProvider = dbProvider;
    }

    public void store(List<ScheduleRecord> records) {
        SQLiteDatabase db = m_dbProvider.getWritableDatabase();
        for (ScheduleRecord r : records) {
            storeRecord(r, db);
        }
    }

    public List<ScheduleRecord> restore() {
        SQLiteDatabase db = m_dbProvider.getReadableDatabase();
        Cursor cursor = db.query(DbProvider.TABLE_NAME, null, null, null, null, null, DbProvider.KEY_ID);
        try {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndex(DbProvider.KEY_ID);
                int dateIdx = cursor.getColumnIndex(DbProvider.KEY_DATE);
                int weekdayIdx = cursor.getColumnIndex(DbProvider.KEY_WEEKDAY);
                int timeIdx = cursor.getColumnIndex(DbProvider.KEY_TIME);
                int typeIdx = cursor.getColumnIndex(DbProvider.KEY_TYPE);
                int courseIdx = cursor.getColumnIndex(DbProvider.KEY_COURSE);
                int roomIdx = cursor.getColumnIndex(DbProvider.KEY_ROOM);
                int lecturerIdx = cursor.getColumnIndex(DbProvider.KEY_LECTURER);
                int count = cursor.getCount();
                List<ScheduleRecord> records = new ArrayList<>(count);
                do {
                    ScheduleRecord r = new ScheduleRecord();
                    r.id = cursor.getInt(idIdx);
                    r.date = cursor.getString(dateIdx);
                    r.weekday = cursor.getString(weekdayIdx);
                    r.time = cursor.getString(timeIdx);
                    r.type = cursor.getString(typeIdx);
                    r.course = cursor.getString(courseIdx);
                    r.room = cursor.getString(roomIdx);
                    r.lecturer = cursor.getString(lecturerIdx);
                    records.add(r);
                } while (cursor.moveToNext());
                return records;
            } else {
                return new ArrayList<>(0);
            }
        } finally {
            cursor.close();
        }
    }

    private static void storeRecord(ScheduleRecord record, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbProvider.KEY_ID, record.id);
        cv.put(DbProvider.KEY_DATE, record.date);
        cv.put(DbProvider.KEY_WEEKDAY, record.weekday);
        cv.put(DbProvider.KEY_TIME, record.time);
        cv.put(DbProvider.KEY_TYPE, record.type);
        cv.put(DbProvider.KEY_COURSE, record.course);
        cv.put(DbProvider.KEY_ROOM, record.room);
        cv.put(DbProvider.KEY_LECTURER, record.lecturer);
        db.insert(DbProvider.TABLE_NAME, null, cv);
    }
}
