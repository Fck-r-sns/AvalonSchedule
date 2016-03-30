package com.binary_machinery.avalonschedule.utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.binary_machinery.avalonschedule.R;

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
        builder.setSmallIcon(R.drawable.icon);
        builder.setAutoCancel(true);
        builder.setContentTitle(context.getString(R.string.app_name));
        SimpleDateFormat formater = new SimpleDateFormat("hh:mm");
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

    public static int getColorForDate(View view, Date date) {
        Calendar calendar = Calendar.getInstance();
        boolean isToday = Constants.DATE_FORMAT.format(date).equals(Constants.DATE_FORMAT.format(calendar.getTime()));
        int colorId = (isToday) ? R.color.colorNearestCourse : R.color.colorTransparent;
        return view.getResources().getColor(colorId);
    }
}
