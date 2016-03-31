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

package com.binary_machinery.avalonschedule.view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.service.ServiceLauncher;
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

        updateServiceStatusWidget();

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

        final ServiceLauncher serviceLauncher = new ServiceLauncher(this);

        Button startServiceButton = (Button) findViewById(R.id.start_service_button);
        startServiceButton.setOnClickListener(v -> {
            serviceLauncher.start();
            Utils.showToast(this, R.string.service_started);
            updateServiceStatusWidget();
        });

        Button stopServiceButton = (Button) findViewById(R.id.stop_service_button);
        stopServiceButton.setOnClickListener(v -> {
            serviceLauncher.stop();
            Utils.showToast(this, R.string.service_stopped);
            updateServiceStatusWidget();
        });
    }

    private void updateServiceStatusWidget() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        boolean started = prefs.getBoolean(Constants.PREF_IS_SERVICE_ENABLED, false);
        TextView statusView = (TextView) findViewById(R.id.service_status);
        String statusText = getString(started ? R.string.status_enabled : R.string.status_disabled);
        statusView.setText(statusText);
    }

}
