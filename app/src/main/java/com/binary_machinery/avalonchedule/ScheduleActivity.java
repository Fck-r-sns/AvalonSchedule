package com.binary_machinery.avalonchedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.binary_machinery.avalonchedule.data.Schedule;
import com.binary_machinery.avalonchedule.data.ScheduleRecord;
import com.binary_machinery.avalonchedule.tools.DbProvider;
import com.binary_machinery.avalonchedule.tools.ScheduleStorager;
import com.binary_machinery.avalonchedule.tools.ScheduleUpdater;
import com.binary_machinery.avalonschedule.R;

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
            case R.id.menu_test_update:
                testUpdateSchedule();
                break;
            case R.id.menu_settings:
                showSettings();
                break;
            case R.id.menu_about:
                showAbout();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreScheduleFromDb() {
        ScheduleStorager storager = new ScheduleStorager(m_dbProvider);
        Schedule schedule = storager.restore();
        List<ScheduleRecord> records = schedule.getRecords();
        printScheduleRecords(records);
    }

    private void updateSchedule() {
        ScheduleUpdater.ResultReceiver callback = schedule -> {
            ScheduleStorager storager = new ScheduleStorager(m_dbProvider);
            storager.store(schedule);
            List<ScheduleRecord> records = schedule.getRecords();
            printScheduleRecords(records);
        };
        String url = "http://www.avalon.ru/HigherEducation/MasterProgrammingIS/Schedule/Semester3/Groups/?GroupID=12285";
        new ScheduleUpdater(this, url, callback).update();
    }

    private void testUpdateSchedule() {
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void showAbout() {
        // TODO: implement
    }

    private void printScheduleRecords(List<ScheduleRecord> records) {
        ListView list = (ListView) findViewById(R.id.scheduleList);
        ListAdapter adapter = new ArrayAdapter<ScheduleRecord>(ScheduleActivity.this, android.R.layout.simple_list_item_1, records);
        list.setAdapter(adapter);
    }
}
