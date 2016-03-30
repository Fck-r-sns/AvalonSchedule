package com.binary_machinery.avalonschedule.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;
import com.binary_machinery.avalonschedule.tools.DbProvider;
import com.binary_machinery.avalonschedule.tools.ScheduleStorager;
import com.binary_machinery.avalonschedule.tools.ScheduleUpdater;
import com.binary_machinery.avalonschedule.utils.Utils;
import com.binary_machinery.avalonschedule.view.schedule.SchedulePagerAdapter;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ScheduleActivity extends AppCompatActivity {

    int m_nearestCoursePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        GlobalEnvironment env = GlobalEnvironment.getInstance();
        if (env.dbProvider == null) {
            env.dbProvider = new DbProvider(this);
        }
        restoreScheduleFromDb();

        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra(Constants.MESSAGE_EXTRA);
            if (message != null) {
                Utils.showMessageDialog(this, message);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_go_to_nearest_course:
                scrollToNearestCourse();
                break;
            case R.id.menu_update:
                updateSchedule();
                break;
            case R.id.menu_changes:
                showChanges();
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
        DbProvider dbProvider = GlobalEnvironment.getInstance().dbProvider;
        ScheduleStorager storager = new ScheduleStorager(dbProvider);
        Schedule schedule = storager.restoreSchedule();
        List<ScheduleRecord> records = schedule.getRecords();
        m_nearestCoursePosition = findNearestCourse(records);
        printScheduleRecords(records);
    }

    private void scrollToNearestCourse() {
        ViewPager pager = (ViewPager) findViewById(R.id.schedulePager);
        pager.setCurrentItem(4, true);
    }

    private void updateSchedule() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        String sourceUrl = prefs.getString(Constants.PREF_URL, "");
        Utils.showToast(this, R.string.task_loading_in_process);
        Observable.just(sourceUrl)
                .concatMap(ScheduleUpdater::get)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        schedule -> {
                            List<ScheduleRecord> records = schedule.getRecords();
                            m_nearestCoursePosition = findNearestCourse(records);
                            printScheduleRecords(records);
                        },
                        throwable -> {
                            Utils.showMessageDialog(this, getString(R.string.task_loading_failed) + ": " + throwable.getMessage());
                        },
                        () -> Utils.showToast(this, R.string.task_loading_finished)
                );
    }

    private void showChanges() {
        Intent settingsIntent = new Intent(this, ChangesActivity.class);
        startActivity(settingsIntent);
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void showAbout() {
        Intent settingsIntent = new Intent(this, AboutActivity.class);
        startActivity(settingsIntent);
    }

    private void printScheduleRecords(List<ScheduleRecord> records) {
        SchedulePagerAdapter adapter = new SchedulePagerAdapter(this, getSupportFragmentManager(), records);
        ViewPager pager = (ViewPager) findViewById(R.id.schedulePager);
        pager.setAdapter(adapter);
    }

    private static int findNearestCourse(List<ScheduleRecord> records) {
        int nearestCoursePosition = 0;
        if (!records.isEmpty()) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            while (records.get(nearestCoursePosition).date.getTime() + Constants.DAY_IN_MILLISECONDS < currentTime) {
                ++nearestCoursePosition;
            }
        }
        return nearestCoursePosition;
    }
}
