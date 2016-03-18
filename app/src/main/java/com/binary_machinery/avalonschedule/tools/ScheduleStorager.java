package com.binary_machinery.avalonschedule.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleMetadata;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

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

    public void store(Schedule schedule) {
        SQLiteDatabase db = m_dbProvider.getWritableDatabase();
        try {
            db.beginTransaction();
            try {
                List<ScheduleRecord> records = schedule.getRecords();
                storeRecords(records, db);
                ScheduleMetadata metadata = schedule.getMetadata();
                storeMetadata(metadata, db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            // TODO: show toast
        }
    }

    public Schedule restore() {
        SQLiteDatabase db = m_dbProvider.getReadableDatabase();
        Schedule schedule = new Schedule();
        try {
            {
                Cursor cursor = db.query(DbProvider.Records.TABLE_NAME, null, null, null, null, null, DbProvider.Records.COLUMN_ID);
                try {
                    if (cursor.moveToFirst()) {
                        int idIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_ID);
                        int dateIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_DATE);
                        int weekdayIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_WEEKDAY);
                        int timeIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_TIME);
                        int typeIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_TYPE);
                        int courseIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_COURSE);
                        int roomIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_ROOM);
                        int lecturerIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_LECTURER);
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
                        schedule.setRecords(records);
                    } else {
                        schedule.setRecords(new ArrayList<ScheduleRecord>(0));
                    }
                } finally {
                    cursor.close();
                }
            }
            {
                Cursor cursor = db.query(DbProvider.Metadata.TABLE_NAME, null, null, null, null, null, null);
                try {
                    ScheduleMetadata metadata = new ScheduleMetadata();
                    if (cursor.moveToFirst()) {
                        int urlIds = cursor.getColumnIndex(DbProvider.Metadata.COLUMN_URL);
                        metadata.url = cursor.getString(urlIds);
                    } else {
                        metadata.url = "";
                    }
                    schedule.setMetadata(metadata);
                } finally {
                    cursor.close();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            schedule.setMetadata(new ScheduleMetadata());
            schedule.setRecords(new ArrayList<ScheduleRecord>(0));
            // TODO: show toast
        }
        return schedule;
    }

    private static void storeMetadata(ScheduleMetadata metadata, SQLiteDatabase db) {
        db.delete(DbProvider.Metadata.TABLE_NAME, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DbProvider.Metadata.COLUMN_ID, DbProvider.Metadata.ID);
        cv.put(DbProvider.Metadata.COLUMN_URL, metadata.url);
        db.insert(DbProvider.Metadata.TABLE_NAME, null, cv);
    }

    private static void storeRecords(List<ScheduleRecord> records, SQLiteDatabase db) {
        db.delete(DbProvider.Records.TABLE_NAME, null, null);
        for (ScheduleRecord record : records) {
            ContentValues cv = new ContentValues();
            cv.put(DbProvider.Records.COLUMN_ID, record.id);
            cv.put(DbProvider.Records.COLUMN_DATE, record.date);
            cv.put(DbProvider.Records.COLUMN_WEEKDAY, record.weekday);
            cv.put(DbProvider.Records.COLUMN_TIME, record.time);
            cv.put(DbProvider.Records.COLUMN_TYPE, record.type);
            cv.put(DbProvider.Records.COLUMN_COURSE, record.course);
            cv.put(DbProvider.Records.COLUMN_ROOM, record.room);
            cv.put(DbProvider.Records.COLUMN_LECTURER, record.lecturer);
            db.insert(DbProvider.Records.TABLE_NAME, null, cv);
        }
    }
}
