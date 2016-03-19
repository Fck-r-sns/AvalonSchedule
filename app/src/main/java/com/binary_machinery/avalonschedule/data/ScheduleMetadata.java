package com.binary_machinery.avalonschedule.data;

/**
 * Created by fckrsns on 13.03.2016.
 */
public class ScheduleMetadata {
    public String url;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (ScheduleMetadata.class != o.getClass()) {
            return false;
        }
        ScheduleMetadata other = (ScheduleMetadata) o;
        return url.equals(other.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
