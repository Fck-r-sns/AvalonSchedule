package com.binary_machinery.polytechschedule.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.binary_machinery.polytechschedule.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleLoader extends AsyncTask<String, Void, Document> {

    public interface ResultReceiver {
        void receive(Document doc);
    }

    private Context m_context;
    private ResultReceiver m_resultReceiver;

    public ScheduleLoader(Context context, ResultReceiver receiver) {
        m_context = context;
        m_resultReceiver = receiver;
    }

    @Override
    protected Document doInBackground(String... urlStrings) {
        String urlString = urlStrings[0];
        try {
            return Jsoup.connect(urlString).get();
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
    protected void onPostExecute(Document doc) {
        if (doc == null) {
            Toast.makeText(m_context, R.string.task_loading_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(m_context, R.string.task_loading_finished, Toast.LENGTH_SHORT).show();
            m_resultReceiver.receive(doc);
        }
    }
}
