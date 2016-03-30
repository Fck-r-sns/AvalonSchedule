package com.binary_machinery.avalonschedule.view;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.service.UpdaterService;
import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.utils.Utils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor prefEditor = prefs.edit();

        EditText urlInput = (EditText) findViewById(R.id.url_input);
        String previousUrl = prefs.getString(Constants.PREF_URL, "");
        urlInput.setText(previousUrl);

        Button saveUrlButton = (Button) findViewById(R.id.save_url_button);
        saveUrlButton.setOnClickListener(v -> {
            String newUrl = urlInput.getText().toString();
            prefEditor.putString(Constants.PREF_URL, newUrl);
            prefEditor.apply();
            Utils.showToast(this, R.string.url_saved);
        });

        final Intent serviceLaunchIntent = new Intent(SettingsActivity.this, UpdaterService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(SettingsActivity.this, 0, serviceLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Button startServiceButton = (Button) findViewById(R.id.start_service_button);
        startServiceButton.setOnClickListener(v -> {
            prefEditor.putBoolean(Constants.PREF_IS_SERVICE_ENABLED, true);
            prefEditor.apply();
            am.setInexactRepeating(
                    AlarmManager.RTC,
                    System.currentTimeMillis(),
                    Constants.UPDATE_INTERVAL,
                    pendingIntent
            );
            Utils.showToast(this, R.string.service_started);
        });

        Button stopServiceButton = (Button) findViewById(R.id.stop_service_button);
        stopServiceButton.setOnClickListener(v -> {
            prefEditor.putBoolean(Constants.PREF_IS_SERVICE_ENABLED, false);
            prefEditor.apply();
            am.cancel(pendingIntent);
            Utils.showToast(this, R.string.service_stopped);
        });
    }

}
