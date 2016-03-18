package com.binary_machinery.avalonchedule.tools;

import com.binary_machinery.avalonchedule.data.Schedule;
import com.binary_machinery.avalonchedule.data.ScheduleMetadata;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {
    public static Observable<Schedule> get(String sourceUrl) {
        return ScheduleLoader.load(sourceUrl)
                .concatMap(ScheduleParser::parse)
                .observeOn(AndroidSchedulers.mainThread())
                .map(records -> {
                    Schedule schedule = new Schedule();
                    ScheduleMetadata metadata = new ScheduleMetadata();
                    metadata.url = sourceUrl;
                    schedule.setMetadata(metadata);
                    schedule.setRecords(records);
                    return schedule;
                });
    }
}
