package com.binary_machinery.polytechschedule.logic;

import java.util.ArrayList;
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

    public static class Record {
        public int id;
        public String date;
        public String time;
        public String type;
        public String course;
        public String room;
        public String lecturer;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(id).append(": ").append(date).append(" ").append(time).append('\n')
                    .append(type).append(", ").append(room).append('\n')
                    .append(course).append('\n')
                    .append(lecturer);
            return builder.toString();
        }
    }

    public String[] header;
    public List<Record> data;
}
