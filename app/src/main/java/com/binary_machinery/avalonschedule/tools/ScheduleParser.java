package com.binary_machinery.avalonschedule.tools;

import com.binary_machinery.avalonschedule.Constants;
import com.binary_machinery.avalonschedule.data.ScheduleColumn;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleParser {

    public static Observable<List<ScheduleRecord>> parse(Document document) {
        return Observable.just(document)
                .subscribeOn(Schedulers.computation())
                .map(doc -> doc.select(".schedule").get(0))
                .map(table -> table.getElementsByTag("tr"))
                .concatMap(elements -> Observable.from(elements.subList(0, elements.size())))
                .skip(1) // skip table header
                .map(ScheduleParser::parseRecord)
                .collect(() -> new ArrayList<ScheduleRecord>(64), List::add);
    }

    private static ScheduleRecord parseRecord(Element element) {
        ScheduleRecord record = new ScheduleRecord();
        SimpleDateFormat dateFormat = Constants.DATE_FORMAT;
        Elements columns = element.getElementsByTag("td");
        try {
            record.id = Integer.parseInt(columns.get(ScheduleColumn.Id.ordinal()).text());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            record.id = -1;
        }

        String dateString = element.getElementsByTag("th").text(); // date and weekday use header tag instead of normal row
        if (dateString.isEmpty()) {
            record.date = new Date();
            record.weekday = Constants.PARSING_ERROR;
        } else {
            String[] dateStringSplit = dateString.split(" ");
            try {
                record.date = dateFormat.parse(dateStringSplit[0]);
            } catch (ParseException e) {
                record.date = new Date();
            }
            record.weekday = dateStringSplit[1];
        }

        // date uses header tags (<th>) and breaks column ordering when row parsed by <td>
        String rawTime = columns.get(ScheduleColumn.Time.ordinal() - 2).html();
        rawTime = rawTime.replace("<sup class=\"min\">", ".");
        record.time = Jsoup.parse(rawTime).text();
        record.type = columns.get(ScheduleColumn.Type.ordinal() - 2).text();
        record.course = columns.get(ScheduleColumn.Course.ordinal() - 2).text();
        record.room = columns.get(ScheduleColumn.Room.ordinal() - 2).text();
        record.lecturer = columns.get(ScheduleColumn.Lecturer.ordinal() - 2).text();
        return record;
    }
}
