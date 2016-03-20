package com.binary_machinery.avalonschedule;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

public class OnBootStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean autoupdaterIsEnabled = prefs.getBoolean(Constants.PREF_IS_SERVICE_ENABLED, false);
        if (autoupdaterIsEnabled) {
            createNotification(context);
            launchScheduleAutoupdater(context);
        }
    }

    private void createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);

        String notificationTitle = context.getString(R.string.app_name);
        builder.setContentTitle(notificationTitle);
        String notificationText = context.getString(R.string.on_boot_notification);
        builder.setContentText(notificationText);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());
    }

    private void launchScheduleAutoupdater(Context context) {
        final Intent serviceLaunchIntent = new Intent(context, UpdaterService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + Constants.UPDATE_INTERVAL,
                Constants.UPDATE_INTERVAL,
                pendingIntent
        );
    }
}
