package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllEventActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_event);

        super.setToolBar(getResources().getString(R.string.events));

        AppCompatTextView tvNoEventsMsg = (AppCompatTextView) findViewById(R.id.id_tv_no_events_msg);
        GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewAllEvents);

        final JSONArray jsonArrayAllEvents = HomeActivity.jsonArrayAllEvents;

        if(jsonArrayAllEvents.length() > 0) {
            JSONArray reverseArray = new JSONArray();

            for( int j = jsonArrayAllEvents.length()-1 ; j >= 0; j--  ) {
                JSONObject obj = new JSONObject();
                try {
                    obj = jsonArrayAllEvents.getJSONObject(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                reverseArray.put(obj);
            }
            RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this, reverseArray);
            gridviewRecentFloc.setAdapter(adapterRecent);

            gridviewRecentFloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intentFlocDesc = new Intent(AllEventActivity.this, FlocDescriptionActivity.class);
                        intentFlocDesc.putExtra("floc_data", jsonArrayAllEvents.getJSONObject(position).toString());
                        intentFlocDesc.putExtra("from", "Event");
                        startActivity(intentFlocDesc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            tvNoEventsMsg.setVisibility(View.VISIBLE);
            gridviewRecentFloc.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
