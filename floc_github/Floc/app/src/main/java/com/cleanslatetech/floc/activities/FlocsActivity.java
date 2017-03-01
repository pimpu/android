package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.GetFlocAsyncTask;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class FlocsActivity extends BaseAppCompactActivity {
    RecyclerView recyclerRunningFloc, recyclerCompletedFloc, recyclerPauseFloc, recyclerCancelFloc,
                        recyclerRequestedFloc, recyclerMyFloc;
    ProgressBar progressBar;
    AppCompatButton btnRefresh;
    AppCompatSpinner spinnerFlocName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flocs);

        super.setToolBar(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {

        recyclerRunningFloc = (RecyclerView) findViewById(R.id.recyclerview_runningFloc);
        recyclerCompletedFloc = (RecyclerView) findViewById(R.id.recyclerview_completedFloc);
        recyclerPauseFloc = (RecyclerView) findViewById(R.id.recyclerview_pauseFloc);
        recyclerCancelFloc = (RecyclerView) findViewById(R.id.recyclerview_cancelFloc);
        recyclerRequestedFloc = (RecyclerView) findViewById(R.id.recyclerview_RequestToJoinFloc);
        recyclerMyFloc = (RecyclerView) findViewById(R.id.recyclerview_MyFlocBooking);

        progressBar = (ProgressBar) findViewById(R.id.runningProgress);
        btnRefresh = (AppCompatButton) findViewById(R.id.btnRefreshRunningPage);

        spinnerFlocName = (AppCompatSpinner) findViewById(R.id.id_spinner_flocName);

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_floc_data_panel).setVisibility(View.GONE);
            findViewById(R.id.layout_noInternet).setVisibility(View.VISIBLE);
            // stop executing code by return
            return;
        } else {
            findViewById(R.id.layout_noInternet).setVisibility(View.GONE);


            new GetFlocAsyncTask(
                    FlocsActivity.this,
                    recyclerRunningFloc,
                    recyclerCompletedFloc,
                    recyclerPauseFloc,
                    recyclerCancelFloc,
                    recyclerRequestedFloc,
                    recyclerMyFloc,
                    progressBar,
                    btnRefresh,
                    spinnerFlocName ).getData();
        }

    }
}
