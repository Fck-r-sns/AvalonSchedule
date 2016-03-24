package com.binary_machinery.avalonschedule;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.tools.DbProvider;
import com.binary_machinery.avalonschedule.tools.ScheduleUpdater;

import java.util.Calendar;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UpdaterService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GlobalEnvironment env = GlobalEnvironment.getInstance();
        if (env.dbProvider == null) {
            env.dbProvider = new DbProvider(this);
        }

        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        String sourceUrl = prefs.getString(Constants.PREF_URL, "");

        if (sourceUrl.isEmpty()) {
            Toast.makeText(this, R.string.task_loading_failed, Toast.LENGTH_SHORT).show();
        } else {
            Observable.just(sourceUrl)
                    .concatMap(ScheduleUpdater::get)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            schedule -> {
                                if (env.addedRecords.size() != 0 || env.deletedRecords.size() != 0) {
                                    createNotification(getString(R.string.on_schedule_changed_notification));
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(Constants.PREF_SCHEDULE_CHANGED, true);
                                } else {
                                    createNotification(getString(R.string.on_schedule_no_changes_notification));
                                }
                            },
                            throwable -> createNotification(getString(R.string.task_loading_failed))
                    );
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(Calendar.getInstance().getTime().toString() + ": " + text);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());
    }
}
