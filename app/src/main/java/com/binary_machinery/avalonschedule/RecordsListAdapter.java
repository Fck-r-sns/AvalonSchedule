package com.binary_machinery.avalonschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.binary_machinery.avalonschedule.data.ScheduleRecord;

import java.util.List;

/**
 * Created by fckrsns on 25.03.2016.
 */
public class RecordsListAdapter extends BaseAdapter {

    Context m_context;
    List<ScheduleRecord> m_records;
    LayoutInflater m_inflater;
    int m_nearestCoursePosition;

    public RecordsListAdapter(Context context, List<ScheduleRecord> records, int nearestCoursePosition) {
        m_context = context;
        m_records = records;
        m_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_nearestCoursePosition = nearestCoursePosition;
    }

    @Override
    public int getCount() {
        return m_records.size();
    }

    @Override
    public Object getItem(int position) {
        return m_records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = m_inflater.inflate(R.layout.records_list_element, parent, false);
        }
        ScheduleRecord record = (ScheduleRecord) getItem(position);
        setText(view, R.id.record_date, Constants.DATE_FORMAT.format(record.date));
        setText(view, R.id.record_weekday, record.weekday);
        setText(view, R.id.record_time, record.time);
        setText(view, R.id.record_type, record.type);
        setText(view, R.id.record_room, record.room);
        setText(view, R.id.record_course, record.course);
        setText(view, R.id.record_lecturer, record.lecturer);

        int colorId = (position == m_nearestCoursePosition) ? R.color.colorNearestCourse : R.color.colorTransparent;
        int color = view.getResources().getColor(colorId);
        view.findViewById(R.id.record_root_layout).setBackgroundColor(color);

        return view;
    }

    private void setText(View view, int id, String text) {
        TextView tv = (TextView) view.findViewById(id);
        tv.setText(text);
    }
}
