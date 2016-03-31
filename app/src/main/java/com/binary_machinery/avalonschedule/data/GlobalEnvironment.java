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
