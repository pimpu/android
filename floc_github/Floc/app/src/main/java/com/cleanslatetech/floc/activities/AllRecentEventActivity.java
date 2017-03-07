package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;

import org.json.JSONException;

public class AllRecentEventActivity extends BaseAppCompactActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_event);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewAllRecentFloc);
        RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this, HomeActivity.jsonArrayAllRecent);
        gridviewRecentFloc.setAdapter(adapterRecent);

        gridviewRecentFloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intentFlocDesc = new Intent(AllRecentEventActivity.this, FlocDescriptionActivity.class);
                    intentFlocDesc.putExtra("floc_data", HomeActivity.jsonArrayAllRecent.getJSONObject(position).toString());
                    intentFlocDesc.putExtra("from", "RecentFloc");
                    startActivity(intentFlocDesc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
