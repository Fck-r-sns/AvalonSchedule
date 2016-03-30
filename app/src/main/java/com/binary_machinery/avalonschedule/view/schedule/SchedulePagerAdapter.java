package com.binary_machinery.avalonschedule.view.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fckrsns on 30.03.2016.
 */
public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    Context m_context;
    List<ScheduleRecord> m_records;

    public SchedulePagerAdapter(Context context, FragmentManager fm, List<ScheduleRecord> records) {
        super(fm);
        m_context = context;
        m_records = records;
    }

    @Override
    public Fragment getItem(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        long from = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        long to = calendar.getTimeInMillis();

        int firstDayIdx = 0;
        int lastDayIdx = 0;
        for (int i = 0; i < m_records.size(); ++i) {
            long time = m_records.get(i).date.getTime();
            if (time < from) {
                firstDayIdx = i;
            } else {
                break;
            }
        }
        for (int i = firstDayIdx; i < m_records.size(); ++i) {
            long time = m_records.get(i).date.getTime();
            if (time < to) {
                lastDayIdx = i;
            } else {
                break;
            }
        }

        Map<Integer, ScheduleRecord> recordsByWeekday = new HashMap<>(7);
        for (int i = firstDayIdx; i <= lastDayIdx; ++i) {
            ScheduleRecord record = m_records.get(i);
            Calendar c = Calendar.getInstance();
            c.setTime(record.date);
            recordsByWeekday.put(c.get(Calendar.DAY_OF_WEEK), record);
        }

        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; ++i) {
            if (!recordsByWeekday.containsKey(i)) {
                ScheduleRecord stubRecord = new ScheduleRecord();
                calendar.set(Calendar.DAY_OF_WEEK, i);
                stubRecord.date = calendar.getTime();
                stubRecord.weekday = m_context.getResources().getStringArray(R.array.weekdays)[i - 1];
                recordsByWeekday.put(i, stubRecord);
            }
        }

        Fragment fragment = new SchedulePageFragment();
        Bundle args = new Bundle();
        for (Map.Entry<Integer, ScheduleRecord> entry : recordsByWeekday.entrySet()) {
            int weekdayIdx = entry.getKey();
            ScheduleRecord record = entry.getValue();
            String key = SchedulePageFragment.ARG_DAY + weekdayIdx;
            args.putParcelable(key, record);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page #" + position;
    }
}