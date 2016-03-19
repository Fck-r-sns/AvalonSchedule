package com.binary_machinery.avalonschedule;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent serviceLaunchIntent = new Intent(SettingsActivity.this, UpdaterService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(SettingsActivity.this, 0, serviceLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Button startServiceButton = (Button) findViewById(R.id.start_service_button);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startService(new Intent(SettingsActivity.this, UpdaterService.class));
                am.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME,
                        0,
                        10000,
                        pendingIntent
                );
            }
        });

        Button stopServiceButton = (Button) findViewById(R.id.stop_service_button);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.cancel(pendingIntent);
            }
        });
    }

}
