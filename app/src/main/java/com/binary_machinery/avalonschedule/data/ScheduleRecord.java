package com.binary_machinery.avalonschedule.data;

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

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (ScheduleRecord.class != o.getClass()) {
            return false;
        }
        ScheduleRecord other = (ScheduleRecord) o;
        // ignore id
        return date.equals(other.date)
                && weekday.equals(other.weekday)
                && time.equals(other.time)
                && type.equals(other.type)
                && course.equals(other.course)
                && room.equals(other.room)
                && lecturer.equals(other.lecturer);
    }

    @Override
    public int hashCode() {
        final int COEF = 31;
        int res = 0;
        res = COEF * res + date.hashCode();
        res = COEF * res + weekday.hashCode();
        res = COEF * res + time.hashCode();
        res = COEF * res + type.hashCode();
        res = COEF * res + course.hashCode();
        res = COEF * res + room.hashCode();
        res = COEF * res + lecturer.hashCode();
        return res;
    }
}
