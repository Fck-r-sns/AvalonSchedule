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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;
import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.utils.Utils;

/**
 * Created by fckrsns on 30.03.2016.
 */
public class EmptyRecordFragment extends Fragment {

    public static final String ARG_RECORD = "record";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_record_list_element, container, false);

        Bundle arguments = getArguments();
        ScheduleRecord record = arguments.getParcelable(ARG_RECORD);
        if (record != null) {
            setText(view, R.id.record_date, Constants.DATE_FORMAT.format(record.date));
            setText(view, R.id.record_weekday, record.weekday);

            int color = Utils.getColorForRecord(view, record);
            view.findViewById(R.id.record_root_layout).setBackgroundColor(color);
        }

        return view;
    }

    private void setText(View view, int id, String text) {
        TextView tv = (TextView) view.findViewById(id);
        tv.setText(text);
    }
}
