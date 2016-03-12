package com.binary_machinery.polytechschedule.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.binary_machinery.polytechschedule.R;
import com.binary_machinery.polytechschedule.data.Schedule;
import com.binary_machinery.polytechschedule.data.ScheduleRecord;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleParser extends AsyncTask<Document, Void, Schedule> {

    public interface ResultReceiver {
        void receive(Schedule schedule);
    }

    private Context m_context;
    private ResultReceiver m_resultReceiver;

    public ScheduleParser(Context context, ResultReceiver receiver) {
        m_context = context;
        m_resultReceiver = receiver;
    }

    @Override
    protected Schedule doInBackground(Document... params) {
        Document doc = params[0];
        Schedule schedule = new Schedule();
        Element table = doc.select(".schedule").get(0);
        Elements rows = table.getElementsByTag("tr");
        Element header = rows.first();
        Elements parsedHeader = header.getElementsByTag("th");
        schedule.header = new String[parsedHeader.size()];
        for (int i = 0; i < parsedHeader.size(); ++i) {
            schedule.header[i] = parsedHeader.get(i).text();
        }
        schedule.data = new ArrayList<>(rows.size() - 1); // -1 for header
        for (int rowIndex = 1; rowIndex < rows.size(); ++rowIndex) {
            ScheduleRecord r = new ScheduleRecord();
            Element row = rows.get(rowIndex);
            Elements columns = row.getElementsByTag("td");
            try {
                r.id = Integer.parseInt(columns.get(Schedule.Column.Id.ordinal()).text());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                r.id = -1;
            }

            Elements currentWeekday = row.select(".currentweekday");
            if (currentWeekday.size() > 0) {
                r.date = currentWeekday.text();
            } else {
                r.date = row.select(".weekday").text();
            }

            // date uses header tags (<th>) and breaks column ordering when row parsed by <td>
            String rawTime = columns.get(Schedule.Column.Time.ordinal() - 1).html();
            rawTime = rawTime.replace("<sup class=\"min\">", ".");
            r.time = Jsoup.parse(rawTime).text();
            r.type = columns.get(Schedule.Column.Type.ordinal() - 1).text();
            r.course = columns.get(Schedule.Column.Course.ordinal() - 1).text();
            r.room = columns.get(Schedule.Column.Room.ordinal() - 1).text();
            r.lecturer = columns.get(Schedule.Column.Lecturer.ordinal() - 1).text();
            schedule.data.add(r);
        }

        return schedule;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(m_context, R.string.task_parsing_in_process, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Schedule schedule) {
        if (schedule == null) {
            Toast.makeText(m_context, R.string.task_parsing_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(m_context, R.string.task_parsing_finished, Toast.LENGTH_SHORT).show();
            m_resultReceiver.receive(schedule);
        }
    }
}
