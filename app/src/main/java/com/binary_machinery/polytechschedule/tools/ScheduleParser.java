package com.binary_machinery.polytechschedule.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.binary_machinery.polytechschedule.R;
import com.binary_machinery.polytechschedule.data.ScheduleColumn;
import com.binary_machinery.polytechschedule.data.ScheduleRecord;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleParser extends AsyncTask<Document, Void, List<ScheduleRecord>> {

    public interface ResultReceiver {
        void receive(List<ScheduleRecord> records);
    }

    private Context m_context;
    private ResultReceiver m_resultReceiver;

    public ScheduleParser(Context context, ResultReceiver receiver) {
        m_context = context;
        m_resultReceiver = receiver;
    }

    @Override
    protected List<ScheduleRecord> doInBackground(Document... params) {
        Document doc = params[0];
        Element table = doc.select(".schedule").get(0);
        Elements rows = table.getElementsByTag("tr");
        List<ScheduleRecord> records = new ArrayList<>(rows.size() - 1); // -1 for header
        // skip header, start with rowIndex = 1
        for (int rowIndex = 1; rowIndex < rows.size(); ++rowIndex) {
            ScheduleRecord r = new ScheduleRecord();
            Element row = rows.get(rowIndex);
            Elements columns = row.getElementsByTag("td");
            try {
                r.id = Integer.parseInt(columns.get(ScheduleColumn.Id.ordinal()).text());
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
            String rawTime = columns.get(ScheduleColumn.Time.ordinal() - 1).html();
            rawTime = rawTime.replace("<sup class=\"min\">", ".");
            r.time = Jsoup.parse(rawTime).text();
            r.type = columns.get(ScheduleColumn.Type.ordinal() - 1).text();
            r.course = columns.get(ScheduleColumn.Course.ordinal() - 1).text();
            r.room = columns.get(ScheduleColumn.Room.ordinal() - 1).text();
            r.lecturer = columns.get(ScheduleColumn.Lecturer.ordinal() - 1).text();
            records.add(r);
        }

        return records;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(m_context, R.string.task_parsing_in_process, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(List<ScheduleRecord> schedule) {
        if (schedule == null) {
            Toast.makeText(m_context, R.string.task_parsing_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(m_context, R.string.task_parsing_finished, Toast.LENGTH_SHORT).show();
            m_resultReceiver.receive(schedule);
        }
    }
}
