package com.binary_machinery.avalonschedule.data;

import com.binary_machinery.avalonschedule.tools.DbProvider;

import java.util.List;

/**
 * Created by fckrsns on 19.03.2016.
 */
public class GlobalEnvironment {

    private static GlobalEnvironment m_instance;
    public DbProvider dbProvider;
    public List<ScheduleRecord> deletedRecords;
    public List<ScheduleRecord> addedRecords;

    private GlobalEnvironment() {
    }

    public static GlobalEnvironment getInstance() {
        if (m_instance == null) {
            m_instance = new GlobalEnvironment();
        }
        return m_instance;
    }
}
