package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;

public class RecentFlocActivity extends BaseAppCompactActivity {

    private RecyclerView rvExperiential, rvProfessional, rvPersonal;
    private RecyclerView.LayoutManager mLayoutManagerEx, mLayoutManagerPro, mLayoutManagerPer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_floc);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {

        rvExperiential = (RecyclerView) findViewById(R.id.horizontal_recyclerview_experientialFloc);
        rvProfessional = (RecyclerView) findViewById(R.id.horizontal_recyclerview_professionalFloc);
        rvPersonal = (RecyclerView) findViewById(R.id.horizontal_recyclerview_persionalFloc);

        // Specify a layout for RecyclerView
        // Create a horizontal RecyclerView
        mLayoutManagerEx = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mLayoutManagerPro = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mLayoutManagerPer = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        rvExperiential.setLayoutManager(mLayoutManagerEx);
        rvProfessional.setLayoutManager(mLayoutManagerPro);
        rvPersonal.setLayoutManager(mLayoutManagerPer);

        // Initialize a new Adapter for RecyclerView
        RecentFlocRecyclerAdapter mAdapter = new RecentFlocRecyclerAdapter(this);

        // Set an adapter for RecyclerView
        rvExperiential.setAdapter(mAdapter);
        rvProfessional.setAdapter(mAdapter);
        rvPersonal.setAdapter(mAdapter);

    }
}
