package com.binary_machinery.polytechschedule.data;

import java.util.List;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class Schedule {
    public enum Column {
        Id,
        Date,
        Time,
        Type,
        Course,
        Room,
        Lecturer,
        COUNT
    }

    public String[] header;
    public List<ScheduleRecord> data;
}
