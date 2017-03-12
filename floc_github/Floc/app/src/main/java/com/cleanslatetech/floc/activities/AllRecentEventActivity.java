package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class AllRecentEventActivity extends BaseAppCompactActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_event);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        String strInterestCategory = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdSelectedCategory));
        String strAllCategory = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdAllCategoryList));
        JSONArray joAllCategory = null;

        try {
            joAllCategory = new JSONArray(strAllCategory);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AppCompatTextView tvNoEventsMsg = (AppCompatTextView) findViewById(R.id.id_tv_no_recent_events_msg);
        ScrollView scrollViewRecentFloc = (ScrollView) findViewById(R.id.id_scrollview_allRecent);
        LinearLayout ll = (LinearLayout) findViewById(R.id.id_ll_recent);

        final JSONArray jsonArrayAllRecent = HomeActivity.jsonArrayAllRecent;

        if(jsonArrayAllRecent.length() > 0) {

            HashMap<Integer, JSONArray> hmapInterest = new HashMap<Integer, JSONArray>();
            HashMap<Integer, JSONArray> hmapNotInterest= new HashMap<Integer, JSONArray>();

            for( int j = 0 ; j < jsonArrayAllRecent.length() ; j++  ) {
                try {

                    int eventCategoryId = jsonArrayAllRecent.getJSONObject(j).getInt("EventCategory");

                    JSONArray jsonArray = new JSONArray();

                    if (strInterestCategory.contains(""+eventCategoryId) ) {

                        boolean isEventCategory = hmapInterest.containsKey(eventCategoryId);

                        if(isEventCategory) {
                            jsonArray = hmapInterest.get(eventCategoryId);
                        }

                        jsonArray.put(jsonArrayAllRecent.getJSONObject(j));
                        hmapInterest.put(eventCategoryId, jsonArray);
                    }
                    else {
                        boolean isEventCategory = hmapNotInterest.containsKey(eventCategoryId);

                        if(isEventCategory) {
                            jsonArray = hmapNotInterest.get(eventCategoryId);
                        }

                        jsonArray.put(jsonArrayAllRecent.getJSONObject(j));
                        hmapNotInterest.put(eventCategoryId, jsonArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            for ( Integer key : hmapInterest.keySet() ) {
                String categoryName = "";

                for (int k = 0; k < joAllCategory.length(); k ++) {
                    try {
                        if(key == joAllCategory.getJSONObject(k).getInt("EventCategoryId")) {
                            categoryName = joAllCategory.getJSONObject(k).getString("EventCategoryName");
                            joAllCategory.remove(k);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                AppCompatTextView txAppCompatTextView = new AppCompatTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 5);
                txAppCompatTextView.setLayoutParams(layoutParams);
                txAppCompatTextView.setPadding(0, 3, 0, 3);
                txAppCompatTextView.setText(categoryName);
                txAppCompatTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txAppCompatTextView.setTextColor(getResources().getColor(R.color.white));
                txAppCompatTextView.setTextSize(20);
                txAppCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
                txAppCompatTextView.setTypeface(font);

                RecyclerView recyclerView = new RecyclerView(this);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerView.setLayoutManager(mLayoutManager);
                // Initialize a new Adapter for RecyclerView
                RecentFlocRecyclerAdapter mAdapter = new RecentFlocRecyclerAdapter(this, hmapInterest.get(key));
                recyclerView.setAdapter(mAdapter);

                ll.addView(txAppCompatTextView);
                ll.addView(recyclerView);
            }


            for ( Integer key : hmapNotInterest.keySet() ) {
                String categoryName = "";

                for (int k = 0; k < joAllCategory.length(); k ++) {
                    try {
                        if(key == joAllCategory.getJSONObject(k).getInt("EventCategoryId")) {
                            categoryName = joAllCategory.getJSONObject(k).getString("EventCategoryName");
                            joAllCategory.remove(k);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                AppCompatTextView txAppCompatTextView = new AppCompatTextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 5);
                txAppCompatTextView.setLayoutParams(layoutParams);
                txAppCompatTextView.setPadding(0, 3, 0, 3);
                txAppCompatTextView.setText(categoryName);
                txAppCompatTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txAppCompatTextView.setTextColor(getResources().getColor(R.color.white));
                txAppCompatTextView.setTextSize(20);
                txAppCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
                txAppCompatTextView.setTypeface(font);

                RecyclerView recyclerView = new RecyclerView(this);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerView.setLayoutManager(mLayoutManager);
                // Initialize a new Adapter for RecyclerView
                RecentFlocRecyclerAdapter mAdapter = new RecentFlocRecyclerAdapter(this, hmapNotInterest.get(key));
                recyclerView.setAdapter(mAdapter);

                ll.addView(txAppCompatTextView);
                ll.addView(recyclerView);
            }
        }
        else {
            tvNoEventsMsg.setVisibility(View.VISIBLE);
            scrollViewRecentFloc.setVisibility(View.GONE);
        }

        /*GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewAllRecentFloc);
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
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
