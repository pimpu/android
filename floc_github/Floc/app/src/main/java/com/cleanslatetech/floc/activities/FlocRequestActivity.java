package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.GetFlocAsyncTask;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class FlocRequestActivity extends BaseAppCompactActivity {
    RecyclerView recyclerRequestedFloc;
    ProgressBar progressBar;
    AppCompatButton btnRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_request);

        super.setToolBar(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        recyclerRequestedFloc = (RecyclerView) findViewById(R.id.all_request_floc_recycler);
        progressBar = (ProgressBar) findViewById(R.id.requesedtFlocProgress);
        btnRefresh = (AppCompatButton) findViewById(R.id.btnRefreshAllRequestFlocPage);

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_noInternet_requestedFloc).setVisibility(View.VISIBLE);
            // stop executing code by return
            return;
        } else {
            findViewById(R.id.layout_noInternet_requestedFloc).setVisibility(View.GONE);
            new GetFlocAsyncTask( FlocRequestActivity.this, recyclerRequestedFloc, progressBar, btnRefresh ).getData();
        }
    }
}
