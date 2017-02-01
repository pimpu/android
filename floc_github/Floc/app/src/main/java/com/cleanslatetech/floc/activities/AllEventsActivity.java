package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.GetAllEventsAsyncTask;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class AllEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        initToolbar();

        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoInternet),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
            new GetAllEventsAsyncTask( AllEventsActivity.this ).getData();
        }

    }

    private void initToolbar() {
        Toolbar allEventToolbar = (Toolbar) findViewById(R.id.id_toolbar_allEvent);
        allEventToolbar.setTitle("All Events");
        setSupportActionBar(allEventToolbar);
    }
}
