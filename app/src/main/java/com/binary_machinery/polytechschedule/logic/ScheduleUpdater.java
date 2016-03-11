package com.binary_machinery.polytechschedule.logic;

import android.content.Context;

import org.jsoup.nodes.Document;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {

    private Context m_context;
    private ScheduleLoader.ResultReceiver m_loaderResultReceiver = new ScheduleLoader.ResultReceiver() {
        @Override
        public void receive(Document doc) {
            new ScheduleParser(m_context).execute(doc);
        }
    };

    public ScheduleUpdater(Context context) {
        m_context = context;
    }

    public void update() {
        String url = "http://www.avalon.ru/HigherEducation/MasterProgrammingIS/Schedule/Semester3/Groups/?GroupID=12285";
        new ScheduleLoader(m_context, m_loaderResultReceiver).execute(url);
    }
}
