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

public class FlocCancelActivity extends BaseAppCompactActivity {
    RecyclerView recyclerCancelFloc;
    ProgressBar progressBar;
    AppCompatButton btnRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_cancel);

        super.setToolBar(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        recyclerCancelFloc = (RecyclerView) findViewById(R.id.all_cancel_floc_recycler);
        progressBar = (ProgressBar) findViewById(R.id.cancelFlocProgress);
        btnRefresh = (AppCompatButton) findViewById(R.id.btnRefreshAllCancelFlocPage);

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_noInternet_cancelFloc).setVisibility(View.VISIBLE);
            // stop executing code by return
            return;
        } else {
            findViewById(R.id.layout_noInternet_cancelFloc).setVisibility(View.GONE);
            new GetFlocAsyncTask( FlocCancelActivity.this, recyclerCancelFloc, progressBar, btnRefresh ).getData();
        }
    }
}
