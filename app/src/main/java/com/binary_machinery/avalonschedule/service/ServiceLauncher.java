/*
 * Copyright 2016 Evgeny Prikhodko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
