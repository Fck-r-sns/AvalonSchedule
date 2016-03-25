package com.binary_machinery.avalonschedule.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleMetadata;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class ScheduleStorager {
    private DbProvider m_dbProvider;

    public ScheduleStorager(DbProvider dbProvider) {
        m_dbProvider = dbProvider;
    }

    public void storeSchedule(Schedule schedule) {
        SQLiteDatabase db = m_dbProvider.getWritableDatabase();
        try {
            db.beginTransaction();
            try {
                List<ScheduleRecord> records = schedule.getRecords();
                deleteRecords(DbProvider.Records.Type.Schedule, db);
                storeRecords(records, DbProvider.Records.Type.Schedule, db);
                ScheduleMetadata metadata = schedule.getMetadata();
                storeMetadata(metadata, db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void storeAddedRecords(List<ScheduleRecord> records) {
        storeRecords(records, DbProvider.Records.Type.Added);
    }

    public void deleteAddedRecords() {
        deleteRecords(DbProvider.Records.Type.Added);
    }

    public List<ScheduleRecord> restoreAddedRecords() {
        return restoreRecords(DbProvider.Records.Type.Added);
    }

    public void storeDeletedRecords(List<ScheduleRecord> records) {
        storeRecords(records, DbProvider.Records.Type.Deleted);
    }

    public void deleteDeletedRecords() {
        deleteRecords(DbProvider.Records.Type.Deleted);
    }

    public List<ScheduleRecord> restoreDeletedRecords() {
        return restoreRecords(DbProvider.Records.Type.Deleted);
    }

    public Schedule restoreSchedule() {
        SQLiteDatabase db = m_dbProvider.getReadableDatabase();
        Schedule schedule = new Schedule();
        try {
            List<ScheduleRecord> records = restoreRecords(DbProvider.Records.Type.Schedule, db);
            schedule.setRecords(records);
            ScheduleMetadata metadata = restoreMetadata(db);
            schedule.setMetadata(metadata);
        } catch (Throwable e) {
            e.printStackTrace();
            schedule.setMetadata(new ScheduleMetadata());
            schedule.setRecords(new ArrayList<ScheduleRecord>(0));
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

    private void storeRecords(List<ScheduleRecord> records, DbProvider.Records.Type type) {
        SQLiteDatabase db = m_dbProvider.getWritableDatabase();
        try {
            db.beginTransaction();
            try {
                storeRecords(records, type, db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void storeRecords(List<ScheduleRecord> records, DbProvider.Records.Type type, SQLiteDatabase db) {
        for (ScheduleRecord record : records) {
            ContentValues cv = new ContentValues();
            cv.put(DbProvider.Records.COLUMN_RECORD_ID, record.id);
            cv.put(DbProvider.Records.COLUMN_DATE, record.date.getTime());
            cv.put(DbProvider.Records.COLUMN_WEEKDAY, record.weekday);
            cv.put(DbProvider.Records.COLUMN_TIME, record.time);
            cv.put(DbProvider.Records.COLUMN_TYPE, record.type);
            cv.put(DbProvider.Records.COLUMN_COURSE, record.course);
            cv.put(DbProvider.Records.COLUMN_ROOM, record.room);
            cv.put(DbProvider.Records.COLUMN_LECTURER, record.lecturer);
            cv.put(DbProvider.Records.COLUMN_RECORD_TYPE, type.ordinal());
            db.insert(DbProvider.Records.TABLE_NAME, null, cv);
        }
    }

    private static ScheduleMetadata restoreMetadata(SQLiteDatabase db) {
        Cursor cursor = db.query(DbProvider.Metadata.TABLE_NAME, null, null, null, null, null, null);
        try {
            ScheduleMetadata metadata = new ScheduleMetadata();
            if (cursor.moveToFirst()) {
                int urlIds = cursor.getColumnIndex(DbProvider.Metadata.COLUMN_URL);
                metadata.url = cursor.getString(urlIds);
            } else {
                metadata.url = "";
            }
            return metadata;
        } finally {
            cursor.close();
        }
    }

    private List<ScheduleRecord> restoreRecords(DbProvider.Records.Type type) {
        SQLiteDatabase db = m_dbProvider.getReadableDatabase();
        try {
            return restoreRecords(type, db);
        } catch (Throwable e) {
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }

    private static List<ScheduleRecord> restoreRecords(DbProvider.Records.Type type, SQLiteDatabase db) {
        String whereClause = DbProvider.Records.COLUMN_RECORD_TYPE + "=" + type.ordinal();
        Cursor cursor = db.query(DbProvider.Records.TABLE_NAME, null, whereClause, null, null, null, DbProvider.Records.COLUMN_RECORD_ID);
        try {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndex(DbProvider.Records.COLUMN_RECORD_ID);
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
                    r.date = new Date(cursor.getLong(dateIdx));
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
                return new ArrayList<ScheduleRecord>(0);
            }
        } finally {
            cursor.close();
        }
    }

    private void deleteRecords(DbProvider.Records.Type type) {
        SQLiteDatabase db = m_dbProvider.getWritableDatabase();
        try {
            db.beginTransaction();
            try {
                deleteRecords(type, db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecords(DbProvider.Records.Type type, SQLiteDatabase db) {
        db.delete(
                DbProvider.Records.TABLE_NAME,
                DbProvider.Records.COLUMN_RECORD_TYPE + " = " + type.ordinal(),
                null
        );
    }
}
