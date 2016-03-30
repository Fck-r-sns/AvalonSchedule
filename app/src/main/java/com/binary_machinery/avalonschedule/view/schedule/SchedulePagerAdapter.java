package com.binary_machinery.avalonschedule.view.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        long from = getMonday(position).getTimeInMillis();
        long to = getSunday(position).getTimeInMillis();

        int firstDayIdx = (!m_records.isEmpty()) ? 0 : Integer.MAX_VALUE;
        int lastDayIdx = Integer.MIN_VALUE;
        for (int i = 0; i <= m_records.size(); ++i) {
            if (i >= m_records.size()) {
                firstDayIdx = Integer.MAX_VALUE;
                break;
            }
            long time = m_records.get(i).date.getTime();
            if (time < from) {
                firstDayIdx = i;
            } else {
                break;
            }
        }
        for (int i = firstDayIdx; i < m_records.size(); ++i) {
            if (i >= m_records.size()) {
                lastDayIdx = Integer.MIN_VALUE;
                break;
            }
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

        Calendar calendar = getMonday(position);
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
        Calendar monday = getMonday(position);
        Calendar sunday = getSunday(position);
        DateFormat formatter = new SimpleDateFormat("dd.MM");
        return formatter.format(monday.getTime()) + " - " + formatter.format(sunday.getTime());
    }

    private Calendar getMonday(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(m_minDate);
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar;
    }

    private Calendar getSunday(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(m_minDate);
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar;
    }
}