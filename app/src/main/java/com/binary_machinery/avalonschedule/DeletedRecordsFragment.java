package com.binary_machinery.avalonschedule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.data.ScheduleRecord;
import com.binary_machinery.avalonschedule.tools.ScheduleStorager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeletedRecordsFragment extends Fragment {

    View m_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_deleted_records, container, false);

        GlobalEnvironment env = GlobalEnvironment.getInstance();
        ScheduleStorager storager = new ScheduleStorager(env.dbProvider);
        List<ScheduleRecord> records = storager.restoreDeletedRecords();
        printRecords(records);

        return m_view;
    }

    public void printRecords(List<ScheduleRecord> records) {
        ListView list = (ListView) m_view.findViewById(R.id.deletedRecordsList);
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, records);
        list.setAdapter(adapter);
    }
}
