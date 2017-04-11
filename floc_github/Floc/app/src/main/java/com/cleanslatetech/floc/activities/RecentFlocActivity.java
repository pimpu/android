package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Set;

public class RecentFlocActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_floc);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int userId = new GetSharedPreference(this).getInt(getResources().getString(R.string.shrdLoginId));

        Set<String> strInterestCategory = new GetSharedPreference(this).getStringSet(getResources().getString(R.string.shrdSelectedCategory));
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

                    if (strInterestCategory.contains(eventCategoryId) ) {

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


                View inflate = mInflater.inflate(R.layout.category_text_layout, null, false);
                AppCompatTextView tvCategory = (AppCompatTextView) inflate.findViewById(R.id.id_dynamic_category_name);

                inflate.findViewById(R.id.id_because_you_like).setVisibility(View.GONE);
                inflate.findViewById(R.id.id_smiley_face).setVisibility(View.GONE);
                tvCategory.setText(categoryName);

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

                ll.addView(inflate);
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

                View inflate = mInflater.inflate(R.layout.category_text_layout, null, false);
                AppCompatTextView tvCategory = (AppCompatTextView) inflate.findViewById(R.id.id_dynamic_category_name);

                inflate.findViewById(R.id.id_because_you_like).setVisibility(View.GONE);
                inflate.findViewById(R.id.id_smiley_face).setVisibility(View.GONE);
                tvCategory.setText(categoryName);

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

                ll.addView(inflate);
                ll.addView(recyclerView);
            }
        }
        else {
            tvNoEventsMsg.setVisibility(View.VISIBLE);
            scrollViewRecentFloc.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
