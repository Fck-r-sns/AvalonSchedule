package com.binary_machinery.polytechschedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.binary_machinery.polytechschedule.data.DbProvider;
import com.binary_machinery.polytechschedule.data.Schedule;
import com.binary_machinery.polytechschedule.logic.ScheduleParser;
import com.binary_machinery.polytechschedule.logic.ScheduleUpdater;
import com.binary_machinery.polytechschedule.data.ScheduleRecord;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private DbProvider m_dbProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        m_dbProvider = new DbProvider(this);
        restoreScheduleFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update:
                updateSchedule();
                break;
            case R.id.menu_settings:
                showSettings();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreScheduleFromDb() {
        List<ScheduleRecord> records;
        SQLiteDatabase db = m_dbProvider.getReadableDatabase();
        Cursor cursor = db.query(DbProvider.TABLE_NAME, null, null, null, null, null, DbProvider.KEY_ID);
        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DbProvider.KEY_ID);
            int dateIdx = cursor.getColumnIndex(DbProvider.KEY_DATE);
            int timeIdx = cursor.getColumnIndex(DbProvider.KEY_TIME);
            int typeIdx = cursor.getColumnIndex(DbProvider.KEY_TYPE);
            int courseIdx = cursor.getColumnIndex(DbProvider.KEY_COURSE);
            int roomIdx = cursor.getColumnIndex(DbProvider.KEY_ROOM);
            int lecturerIdx = cursor.getColumnIndex(DbProvider.KEY_LECTURER);
            int count = cursor.getCount();
            records = new ArrayList<>(count);
            do {
                ScheduleRecord r = new ScheduleRecord();
                r.id = cursor.getInt(idIdx);
                r.date = cursor.getString(dateIdx);
                r.time = cursor.getString(timeIdx);
                r.type = cursor.getString(typeIdx);
                r.course = cursor.getString(courseIdx);
                r.room = cursor.getString(roomIdx);
                r.lecturer = cursor.getString(lecturerIdx);
                records.add(r);
            } while (cursor.moveToNext());
        } else {
            records = new ArrayList<>(0);
        }
        cursor.close();
        printSchedule(records);
    }

    private void updateSchedule() {
        new ScheduleUpdater(this, new ScheduleParser.ResultReceiver() {
            @Override
            public void receive(Schedule schedule) {
                List<ScheduleRecord> records = schedule.data;
                SQLiteDatabase db = m_dbProvider.getWritableDatabase();
                for (ScheduleRecord r : records) {
                    r.save(db);
                }
                printSchedule(records);
            }
        }).update();
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void printSchedule(List<ScheduleRecord> records) {
        ListView list = (ListView) findViewById(R.id.scheduleList);
        ListAdapter adapter = new ArrayAdapter<ScheduleRecord>(ScheduleActivity.this, android.R.layout.simple_list_item_1, records);
        list.setAdapter(adapter);
    }
}
