package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.cleanslatetech.floc.R;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class FlocPauseActivity extends BaseAppCompactActivity {
    RecyclerView recyclerPauseFloc;
    ProgressBar progressBar;
    AppCompatButton btnRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_pause);

        super.setToolBar(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        recyclerPauseFloc = (RecyclerView) findViewById(R.id.all_pause_floc_recycler);
        progressBar = (ProgressBar) findViewById(R.id.pauseFlocProgress);
        btnRefresh = (AppCompatButton) findViewById(R.id.btnRefreshAllPauseFlocPage);

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_noInternet_pauseFloc).setVisibility(View.VISIBLE);
            // stop executing code by return
            return;
        } else {
            findViewById(R.id.layout_noInternet_pauseFloc).setVisibility(View.GONE);
//            new GetFlocAsyncTask( FlocPauseActivity.this, recyclerRunningFloc, recyclerCompletedFloc, recyclerPauseFloc, recyclerCancelFloc, recyclerRequestedFloc, recyclerPauseFloc, progressBar, btnRefresh ).getData();
        }
    }

}
