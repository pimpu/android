package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.IndividualFlocAdapter;

public class RecentFlocPersonalActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_floc_personal);

        super.setToolBar(getResources().getString(R.string.recent_flocs));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        GridView gridviewPersonal = (GridView) findViewById(R.id.grideview_personal);
        IndividualFlocAdapter individualFlocAdapter = new IndividualFlocAdapter(this);
        gridviewPersonal.setAdapter(individualFlocAdapter);
    }
}
