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
        createNotification();
        GlobalEnvironment env = GlobalEnvironment.getInstance();
        if (env.dbProvider == null) {
            env.dbProvider = new DbProvider(this);
        }
//        String sourceUrl = "http://www.avalon.ru/HigherEducation/MasterProgrammingIS/Schedule/Semester3/Groups/?GroupID=12285";
//        String sourceUrl = "http://www.avalon.ru/HigherEducation/MasterProgrammingIS/Schedule/Semester1/Groups/?GroupID=12085";
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
                            },
                            throwable -> Toast.makeText(this, R.string.task_loading_failed, Toast.LENGTH_SHORT).show()
                    );
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("Notification");
        builder.setContentText(Calendar.getInstance().getTime().toString());

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());
    }
}
