package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.cleanslatetech.floc.R;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class FlocRunningActivity extends BaseAppCompactActivity {
    RecyclerView recyclerRunningFloc;
    ProgressBar progressBar;
    AppCompatButton btnRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_floc);

        super.setToolBar(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        recyclerRunningFloc = (RecyclerView) findViewById(R.id.all_running_floc_recycler);
        progressBar = (ProgressBar) findViewById(R.id.runningFlocProgress);
        btnRefresh = (AppCompatButton) findViewById(R.id.btnRefreshAllEventPage);

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_noInternet).setVisibility(View.VISIBLE);
            // stop executing code by return
            return;
        } else {
            findViewById(R.id.layout_noInternet).setVisibility(View.GONE);
//            new GetFlocAsyncTask( FlocRunningActivity.this, recyclerRunningFloc, recyclerCompletedFloc, recyclerPauseFloc, recyclerCancelFloc, recyclerRequestedFloc, recyclerRunningFloc, progressBar, btnRefresh ).getData();
        }
    }
}
