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

package com.binary_machinery.avalonschedule.view.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.util.Calendar;

import rx.functions.Func2;

/**
 * Created by fckrsns on 30.03.2016.
 */
public class SchedulePageFragment extends Fragment {

    public static final String ARG_DAY = "day";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.schedule_page, container, false);
        Bundle arguments = getArguments();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Func2<Integer, Integer, ?> setRecord = (dayOfWeek, layoutId) -> {
            String key = ARG_DAY + dayOfWeek;
            ScheduleRecord record = arguments.getParcelable(key);

            if (record != null) {
                Fragment fragment = (record.course != null) ? new RecordFragment() : new EmptyRecordFragment();
                Bundle args = new Bundle();
                args.putParcelable(RecordFragment.ARG_RECORD, record);
                fragment.setArguments(args);
                fragmentTransaction.replace(layoutId, fragment);
            }

            return 0;
        };

        setRecord.call(Calendar.MONDAY, R.id.layoutMonday);
        setRecord.call(Calendar.TUESDAY, R.id.layoutTuesday);
        setRecord.call(Calendar.WEDNESDAY, R.id.layoutWednesday);
        setRecord.call(Calendar.THURSDAY, R.id.layoutThursday);
        setRecord.call(Calendar.FRIDAY, R.id.layoutFriday);
        setRecord.call(Calendar.SATURDAY, R.id.layoutSaturday);
        setRecord.call(Calendar.SUNDAY, R.id.layoutSunday);

        fragmentTransaction.commit();
        return rootView;
    }
}
