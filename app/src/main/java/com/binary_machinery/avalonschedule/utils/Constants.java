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

package com.binary_machinery.avalonschedule.utils;

import java.text.SimpleDateFormat;

/**
 * Created by fckrsns on 19.03.2016.
 */
public class Constants {
    public static final long HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
    public static final long DAY_IN_MILLISECONDS = 24 * HOUR_IN_MILLISECONDS;

    public static final String PARSING_ERROR = "Parsing error";
    public static final long UPDATE_INTERVAL = 60 * 60 * 1000; // one hour
    public static final long ALARM_TIME_SINCE_LAST_UPDATE_HOURS = 4;
    public static final long ALARM_TIME_SINCE_LAST_UPDATE_MS = ALARM_TIME_SINCE_LAST_UPDATE_HOURS * HOUR_IN_MILLISECONDS;

    // shared preferences
    public static final String PREFERENCES_NAME = "settings";
    public static final String PREF_URL = "url";
    public static final String PREF_IS_SERVICE_ENABLED = "service_enabled";
    public static final String PREF_SCHEDULE_CHANGED = "schedule_changed";
    public static final String PREF_SCHEDULE_LAST_CHECK_DATE = "schedule_last_change_date";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm:ss");

    public static final String MESSAGE_EXTRA = "message";
}
