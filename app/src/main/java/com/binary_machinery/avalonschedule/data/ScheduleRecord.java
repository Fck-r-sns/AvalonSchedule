package com.binary_machinery.avalonschedule.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.binary_machinery.avalonschedule.utils.Constants;

import java.util.Date;

/**
 * Created by fckrsns on 12.03.2016.
 */
public class ScheduleRecord implements Parcelable {
    public int id;
    public Date date;
    public String weekday;
    public String time;
    public String type;
    public String course;
    public String room;
    public String lecturer;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.DATE_FORMAT.format(date)).append(" ").append(weekday).append(" ").append(time).append('\n')
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(date.getTime());
        dest.writeString(weekday);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeString(course);
        dest.writeString(room);
        dest.writeString(lecturer);
    }

    public static final Parcelable.Creator<ScheduleRecord> CREATOR = new Parcelable.Creator<ScheduleRecord>() {
        @Override
        public ScheduleRecord createFromParcel(Parcel source) {
            ScheduleRecord record = new ScheduleRecord();
            record.id = source.readInt();
            record.date = new Date(source.readLong());
            record.weekday = source.readString();
            record.time = source.readString();
            record.type = source.readString();
            record.course = source.readString();
            record.room = source.readString();
            record.lecturer = source.readString();
            return record;
        }

        @Override
        public ScheduleRecord[] newArray(int size) {
            return new ScheduleRecord[size];
        }
    };
}
