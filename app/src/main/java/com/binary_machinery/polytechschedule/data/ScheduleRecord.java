package com.binary_machinery.polytechschedule.data;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class ScheduleRecord {
    public int id;
    public String date;
    public String weekday;
    public String time;
    public String type;
    public String course;
    public String room;
    public String lecturer;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id).append(": ").append(date).append(" ").append(weekday).append(" ").append(time).append('\n')
                .append(type).append(", ").append(room).append('\n')
                .append(course).append('\n')
                .append(lecturer);
        return builder.toString();
    }
}
