package com.binary_machinery.polytechschedule.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class ScheduleRecord {
    public int id;
    public String date;
    public String time;
    public String type;
    public String course;
    public String room;
    public String lecturer;

    public ScheduleRecord() {
    }

    public ScheduleRecord(int id, String date, String time, String type, String course, String room, String lecturer) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.course = course;
        this.room = room;
        this.lecturer = lecturer;
    }

    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbProvider.KEY_ID, id);
        cv.put(DbProvider.KEY_DATE, date);
        cv.put(DbProvider.KEY_TIME, time);
        cv.put(DbProvider.KEY_TYPE, type);
        cv.put(DbProvider.KEY_COURSE, course);
        cv.put(DbProvider.KEY_ROOM, room);
        cv.put(DbProvider.KEY_LECTURER, lecturer);
        db.insert(DbProvider.TABLE_NAME, null, cv);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id).append(": ").append(date).append(" ").append(time).append('\n')
                .append(type).append(", ").append(room).append('\n')
                .append(course).append('\n')
                .append(lecturer);
        return builder.toString();
    }
}
