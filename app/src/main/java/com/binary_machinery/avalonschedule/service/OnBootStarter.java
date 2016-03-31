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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.utils.Constants;
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
