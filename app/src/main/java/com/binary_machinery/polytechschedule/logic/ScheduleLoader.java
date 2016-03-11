package com.binary_machinery.polytechschedule.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.binary_machinery.polytechschedule.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleLoader extends AsyncTask<String, Void, String> {

    public interface ResultReceiver {
        void receive(String html);
    }

    private Context m_context;
    private ResultReceiver m_resultReceiver;

    public ScheduleLoader(Context context, ResultReceiver receiver) {
        m_context = context;
        m_resultReceiver = receiver;
    }

    @Override
    protected String doInBackground(String... urlStrings) {
        if (urlStrings.length != 1) {
            return null;
        }
        String urlString = urlStrings[0];
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                StringBuilder builder = new StringBuilder();
                String newLine;
                while ((newLine = reader.readLine()) != null) {
                    builder.append(newLine).append('\n');
                }
                return builder.toString();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(m_context, R.string.task_loading_in_process, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(m_context, R.string.task_loading_finished, Toast.LENGTH_SHORT).show();
        m_resultReceiver.receive(s);
    }
}
