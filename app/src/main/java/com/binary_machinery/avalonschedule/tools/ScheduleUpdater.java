/*
 * Copyright 2016 Evgeny Prikhodko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binary_machinery.avalonschedule.tools;

import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleMetadata;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleUpdater {
    public static Observable<Schedule> get(String sourceUrl) {
        GlobalEnvironment env = GlobalEnvironment.getInstance();
        ScheduleStorager storager = new ScheduleStorager(env.dbProvider);
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
                })
                .subscribeOn(Schedulers.io())
                .map(schedule -> {
                    Schedule oldSchedule = storager.restoreSchedule();
                    ScheduleComparator cmp = new ScheduleComparator();
                    boolean equals = cmp.compare(oldSchedule, schedule);
                    env.deletedRecords = cmp.getDeletedRecords();
                    env.addedRecords = cmp.getAddedRecords();
                    if (!equals) {
                        storager.storeSchedule(schedule);
                        storager.storeDeletedRecords(env.deletedRecords);
                        storager.storeAddedRecords(env.addedRecords);
                    }
                    return schedule;
                });
    }
}
