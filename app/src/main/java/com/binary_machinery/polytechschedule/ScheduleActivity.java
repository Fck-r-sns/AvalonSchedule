package com.binary_machinery.polytechschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.binary_machinery.polytechschedule.tools.DbProvider;
import com.binary_machinery.polytechschedule.tools.ScheduleParser;
import com.binary_machinery.polytechschedule.tools.ScheduleStorager;
import com.binary_machinery.polytechschedule.tools.ScheduleUpdater;
import com.binary_machinery.polytechschedule.data.ScheduleRecord;

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
        ScheduleStorager storager = new ScheduleStorager(m_dbProvider);
        List<ScheduleRecord> records = storager.restore();
        printSchedule(records);
    }

    private void updateSchedule() {
        ScheduleParser.ResultReceiver callback = new ScheduleParser.ResultReceiver() {
            @Override
            public void receive(List<ScheduleRecord> records) {
                ScheduleStorager storager = new ScheduleStorager(m_dbProvider);
                storager.store(records);
                printSchedule(records);
            }
        };
        new ScheduleUpdater(this, callback).update();
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
