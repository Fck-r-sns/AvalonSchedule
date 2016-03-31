package com.binary_machinery.avalonschedule.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.binary_machinery.avalonschedule.utils.Constants;

/**
 * Created by fckrsns on 31.03.2016.
 */
public class ServiceLauncher {

    private final Context m_context;
    private final PendingIntent m_intent;
    private final AlarmManager m_alarmManager;

    public ServiceLauncher(Context context) {
        final Intent serviceLaunchIntent = new Intent(context, UpdaterService.class);
        m_intent = PendingIntent.getService(context, 0, serviceLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        m_alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        m_context = context;
    }

    public void start() {
        setServiceEnabledPreference(true);
        m_alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                Constants.UPDATE_INTERVAL,
                m_intent
        );
    }

    public void stop() {
        setServiceEnabledPreference(false);
        m_alarmManager.cancel(m_intent);
    }

    private void setServiceEnabledPreference(boolean value) {
        SharedPreferences prefs = m_context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(Constants.PREF_IS_SERVICE_ENABLED, value);
        prefEditor.apply();
    }
}
