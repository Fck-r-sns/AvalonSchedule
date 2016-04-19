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

package com.binary_machinery.avalonschedule.utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fckrsns on 27.03.2016.
 */
public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static void showNotification(Context context, String message, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.icon_transparent);
        builder.setAutoCancel(true);
        builder.setContentTitle(context.getString(R.string.app_name));
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        String timeString = formater.format(Calendar.getInstance().getTime());
        builder.setContentText(timeString + ": " + message);

        if (intent != null) {
            builder.setContentIntent(intent);
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());
    }

    public static void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok_button_text, (dialog, which) -> {

                });
        builder.create().show();
    }

    public static int getColorForRecord(View view, ScheduleRecord record) {
        Date date = record.date;
        Calendar calendar = Calendar.getInstance();
        boolean isToday = Constants.DATE_FORMAT.format(date).equals(Constants.DATE_FORMAT.format(calendar.getTime()));
        int colorId = R.color.colorTransparent;
        if (isToday) {
            colorId = R.color.colorNearestCourse;
        } else {
            if (record.type != null) {
                String currentTypeString = record.type.trim();
                String examTypeString = view.getResources().getString(R.string.exam_type);
                if (currentTypeString.equals(examTypeString)) {
                    colorId = R.color.colorExamDay;
                }
            }
        }
        return view.getResources().getColor(colorId);
    }
}
