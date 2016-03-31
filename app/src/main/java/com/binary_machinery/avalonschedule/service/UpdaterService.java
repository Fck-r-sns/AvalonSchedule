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

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.tools.DbProvider;
import com.binary_machinery.avalonschedule.tools.ScheduleUpdater;
import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.utils.Utils;
import com.binary_machinery.avalonschedule.view.ChangesActivity;
import com.binary_machinery.avalonschedule.view.ScheduleActivity;

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
            Utils.showToast(this, R.string.task_loading_failed);
        } else {
            Observable.just(sourceUrl)
                    .concatMap(ScheduleUpdater::get)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            schedule -> {
                                if (env.addedRecords.size() != 0 || env.deletedRecords.size() != 0) {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(Constants.PREF_SCHEDULE_CHANGED, true);
                                    Intent changesIntent = new Intent(this, ChangesActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, changesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    Utils.showNotification(this, getString(R.string.on_schedule_changed_notification), pendingIntent);
                                } else {
//                                    Utils.showNotification(this, getString(R.string.on_schedule_no_changes_notification), null);
                                }
                            },
                            throwable -> {
                                Intent messageIntent = new Intent(this, ScheduleActivity.class);
                                messageIntent.putExtra(Constants.MESSAGE_EXTRA, throwable.getMessage());
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                Utils.showNotification(this, throwable.getMessage(), pendingIntent);
                            }
                    );
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
