package com.binary_machinery.polytechschedule.tools;

import android.content.Context;

import com.binary_machinery.polytechschedule.data.Schedule;
import com.binary_machinery.polytechschedule.data.ScheduleMetadata;
import com.binary_machinery.polytechschedule.data.ScheduleRecord;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {

    public interface ResultReceiver {
        void receive(Schedule schedule);
    }

    private Context m_context;
    private String m_url;
    private ResultReceiver m_newScheduleReceiver;
    private ScheduleLoader.ResultReceiver m_loaderResultReceiver = new ScheduleLoader.ResultReceiver() {
        @Override
        public void receive(Document doc) {
            new ScheduleParser(m_context, m_parserResultReceiver).execute(doc);
        }
    };
    private ScheduleParser.ResultReceiver m_parserResultReceiver = new ScheduleParser.ResultReceiver() {
        @Override
        public void receive(List<ScheduleRecord> records) {
            Schedule schedule = new Schedule();
            schedule.setRecords(records);
            ScheduleMetadata metadata = new ScheduleMetadata();
            metadata.url = m_url;
            schedule.setMetadata(metadata);
            m_newScheduleReceiver.receive(schedule);
        }
    };

    public ScheduleUpdater(Context context, String sourceUrl, ResultReceiver receiver) {
        m_context = context;
        m_url = sourceUrl;
        m_newScheduleReceiver = receiver;
    }

    public void update() {
        new ScheduleLoader(m_context, m_loaderResultReceiver).execute(m_url);
    }
}
