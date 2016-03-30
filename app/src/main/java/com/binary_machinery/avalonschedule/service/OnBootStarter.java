package com.binary_machinery.avalonschedule.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.utils.Utils;

public class OnBootStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean autoupdaterIsEnabled = prefs.getBoolean(Constants.PREF_IS_SERVICE_ENABLED, false);
        if (autoupdaterIsEnabled) {
            Utils.showNotification(context, context.getString(R.string.on_boot_notification), null);
            launchScheduleAutoupdater(context);
        }
    }

    private void launchScheduleAutoupdater(Context context) {
        final Intent serviceLaunchIntent = new Intent(context, UpdaterService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                Constants.UPDATE_INTERVAL,
                pendingIntent
        );
    }
}
