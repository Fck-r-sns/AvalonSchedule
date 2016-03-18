package com.binary_machinery.avalonschedule.tools;

import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleMetadata;

import rx.Observable;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {
    public static Observable<Schedule> get(String sourceUrl) {
        return Observable.just(sourceUrl)
                .concatMap(ScheduleLoader::load)
                .concatMap(ScheduleParser::parse)
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
