package com.binary_machinery.polytechschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.binary_machinery.polytechschedule.logic.Schedule;
import com.binary_machinery.polytechschedule.logic.ScheduleParser;
import com.binary_machinery.polytechschedule.logic.ScheduleUpdater;

public class ScheduleActivity extends AppCompatActivity {

    private ListView m_scheduleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        m_scheduleView = (ListView) findViewById(R.id.scheduleList);
        updateSchedule();
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

    private void updateSchedule() {
        new ScheduleUpdater(this, new ScheduleParser.ResultReceiver() {
            @Override
            public void receive(Schedule schedule) {
                ListAdapter adapter = new ArrayAdapter<Schedule.Record>(ScheduleActivity.this, android.R.layout.simple_list_item_1, schedule.data);
                m_scheduleView.setAdapter(adapter);
            }
        }).update();
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
