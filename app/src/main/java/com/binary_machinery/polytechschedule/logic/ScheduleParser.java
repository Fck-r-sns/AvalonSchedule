package com.binary_machinery.polytechschedule.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.binary_machinery.polytechschedule.R;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleParser extends AsyncTask<String, Void, Void> {
    private Context m_context;

    public ScheduleParser(Context context) {
        m_context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String html = params[0];
        return null;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(m_context, R.string.task_parsing_in_process, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(m_context, R.string.task_parsing_finished, Toast.LENGTH_SHORT).show();
    }
}
