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
