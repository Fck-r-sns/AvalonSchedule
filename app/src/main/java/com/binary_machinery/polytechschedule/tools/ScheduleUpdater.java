package com.binary_machinery.polytechschedule.tools;

import android.content.Context;

import org.jsoup.nodes.Document;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {

    private Context m_context;
    private ScheduleParser.ResultReceiver m_scheduleReceiver;
    private ScheduleLoader.ResultReceiver m_loaderResultReceiver = new ScheduleLoader.ResultReceiver() {
        @Override
        public void receive(Document doc) {
            new ScheduleParser(m_context, m_scheduleReceiver).execute(doc);
        }
    };

    public ScheduleUpdater(Context context, ScheduleParser.ResultReceiver receiver) {
        m_context = context;
        m_scheduleReceiver = receiver;
    }

    public void update(String sourceUrl) {
        new ScheduleLoader(m_context, m_loaderResultReceiver).execute(sourceUrl);
    }
}
