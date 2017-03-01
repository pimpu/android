package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.widget.GridView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;

public class RecentFlocExperientalActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_floc_experiental);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        GridView gridviewExperiental = (GridView) findViewById(R.id.grideview_experiential);
        RecentFlocRecyclerAdapter individualFlocAdapter = new RecentFlocRecyclerAdapter(this);
//        gridviewExperiental.setAdapter(individualFlocAdapter);
    }
}
