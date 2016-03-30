package com.binary_machinery.avalonschedule.view.schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binary_machinery.avalonschedule.utils.Constants;
import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;
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

            int color = Utils.getColorForDate(view, record.date);
            view.findViewById(R.id.record_root_layout).setBackgroundColor(color);
        }

        return view;
    }

    private void setText(View view, int id, String text) {
        TextView tv = (TextView) view.findViewById(id);
        tv.setText(text);
    }
}
