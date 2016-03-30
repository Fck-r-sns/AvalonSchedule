package com.binary_machinery.avalonschedule.view.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;
import com.binary_machinery.avalonschedule.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fckrsns on 30.03.2016.
 */
public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    final Context m_context;
    final List<ScheduleRecord> m_records;
    final Date m_minDate;

    public SchedulePagerAdapter(Context context, FragmentManager fm, List<ScheduleRecord> records) {
        super(fm);
        m_context = context;
        m_records = records;
        m_minDate = (!m_records.isEmpty()) ? m_records.get(0).date : Calendar.getInstance().getTime();
    }

    @Override
    public Fragment getItem(int position) {
        long from = getWeekBeginning(position).getTimeInMillis();
        long to = getWeekEnd(position).getTimeInMillis();

        Map<Integer, ScheduleRecord> recordsByWeekday = new HashMap<>(7);
        for (ScheduleRecord record : m_records) {
            long time = record.date.getTime();
            if (from <= time && time <= to) {
                Calendar c = Calendar.getInstance();
                c.setTime(record.date);
                recordsByWeekday.put(c.get(Calendar.DAY_OF_WEEK), record);
            }
        }

        Calendar calendar = getWeekBeginning(position);
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
        return 1000;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar monday = getWeekBeginning(position);
        Calendar sunday = getWeekEnd(position);
        DateFormat formatter = new SimpleDateFormat("dd.MM");
        return formatter.format(monday.getTime()) + " - " + formatter.format(sunday.getTime());
    }

    private Calendar getWeekBeginning(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(m_minDate);
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private Calendar getWeekEnd(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(m_minDate);
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }
}