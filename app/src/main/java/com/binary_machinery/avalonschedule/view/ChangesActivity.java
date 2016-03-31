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

package com.binary_machinery.avalonschedule.view;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.binary_machinery.avalonschedule.R;
import com.binary_machinery.avalonschedule.data.GlobalEnvironment;
import com.binary_machinery.avalonschedule.tools.DbProvider;
import com.binary_machinery.avalonschedule.tools.ScheduleStorager;

import java.util.ArrayList;

public class ChangesActivity extends AppCompatActivity {

    DeletedRecordsFragment m_deletedRecordsFragment;
    AddedRecordsFragment m_addedRecordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GlobalEnvironment env = GlobalEnvironment.getInstance();
        if (env.dbProvider == null) {
            env.dbProvider = new DbProvider(this);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        m_deletedRecordsFragment = new DeletedRecordsFragment();
        fragmentTransaction.replace(R.id.deletedFragmentLayout, m_deletedRecordsFragment);
        m_addedRecordsFragment = new AddedRecordsFragment();
        fragmentTransaction.replace(R.id.addedFragmentLayout, m_addedRecordsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changes_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_changes:
                deleteChangedRecordsFromDb();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteChangedRecordsFromDb() {
        GlobalEnvironment env = GlobalEnvironment.getInstance();
        ScheduleStorager storager = new ScheduleStorager(env.dbProvider);
        storager.deleteDeletedRecords();
        m_deletedRecordsFragment.printRecords(new ArrayList<>(0));
        storager.deleteAddedRecords();
        m_addedRecordsFragment.printRecords(new ArrayList<>(0));
    }
}
