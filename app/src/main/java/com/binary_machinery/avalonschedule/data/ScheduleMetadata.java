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
