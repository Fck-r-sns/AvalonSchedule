package com.binary_machinery.avalonschedule.data;

import java.util.List;

/**
 * Created by fckrsns on 13.03.2016.
 */
public class Schedule {
    private ScheduleMetadata m_metadata;
    private List<ScheduleRecord> m_records;

    public ScheduleMetadata getMetadata() {
        return m_metadata;
    }

    public void setMetadata(ScheduleMetadata metadata) {
        this.m_metadata = metadata;
    }

    public List<ScheduleRecord> getRecords() {
        return m_records;
    }

    public void setRecords(List<ScheduleRecord> records) {
        this.m_records = records;
    }
}
