package com.binary_machinery.avalonschedule.tools;

import com.binary_machinery.avalonschedule.data.Schedule;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by fckrsns on 19.03.2016.
 */
public class ScheduleComparator {
    private List<ScheduleRecord> m_deleted;
    private List<ScheduleRecord> m_added;

    public boolean compare(Schedule oldSchedule, Schedule newSchedule) {
        if (!oldSchedule.getMetadata().equals(newSchedule.getMetadata())) {
            m_added = new ArrayList<>(0);
            m_deleted = new ArrayList<>(0);
            return false;
        }

        HashSet<ScheduleRecord> newSet = new HashSet<>(newSchedule.getRecords());
        newSet.removeAll(oldSchedule.getRecords());
        m_added = new ArrayList<>(newSet);

        HashSet<ScheduleRecord> oldSet = new HashSet<>(oldSchedule.getRecords());
        oldSet.removeAll(newSchedule.getRecords());
        m_deleted = new ArrayList<>(oldSet);

        return m_added.isEmpty() &&  m_deleted.isEmpty();
    }

    public List<ScheduleRecord> getDeletedRecords() {
        return m_deleted;
    }

    public List<ScheduleRecord> getAddedRecords() {
        return m_added;
    }
}
