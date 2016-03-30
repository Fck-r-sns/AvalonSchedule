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
import com.binary_machinery.avalonschedule.view.schedule.EmptyRecordFragment;
import com.binary_machinery.avalonschedule.view.schedule.RecordFragment;

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
