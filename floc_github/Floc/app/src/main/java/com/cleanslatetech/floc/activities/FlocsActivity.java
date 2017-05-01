package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.EventInvitationAsyncTask;
import com.cleanslatetech.floc.asynctask.GetFlocAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.Validations;

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

        super.setToolBar(getResources().getString(R.string.my_flocworld));

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

        getFlocFromServer();

    }

    private void getFlocFromServer() {
        // Check if Internet present
        if (!isConnectingToInternet(getApplicationContext())) {

            findViewById(R.id.layout_floc_data_panel).setVisibility(View.GONE);
            findViewById(R.id.layout_noInternet).setVisibility(View.VISIBLE);

            AppCompatButton btnRetry = (AppCompatButton) findViewById(R.id.id_btn_retry_all_flocs);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFlocFromServer();
                }
            });
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

    public void onClickEventInvitation(View view) {
        if (!isConnectingToInternet(getApplicationContext())) {

            CommonUtilities.customToast(FlocsActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {
            AppCompatEditText txtFriendEmail = (AppCompatEditText) findViewById(R.id.id_txt_friend_to_invite);
            Boolean boolEmail = Validations.emailValidate(txtFriendEmail.getText().toString());

            if (GetFlocAsyncTask.iEventId == 0) {
                CommonUtilities.customToast(FlocsActivity.this, "Please, Select running floc.");
                return;
            }
            if(boolEmail) {
                int iUSerId = new GetSharedPreference(this).getInt(getResources().getString(R.string.shrdLoginId));

                new EventInvitationAsyncTask(FlocsActivity.this,
                        iUSerId,
                        txtFriendEmail.getText().toString(),
                        GetFlocAsyncTask.iEventId).postData();
            }
            else {
                CommonUtilities.customToast(FlocsActivity.this, "Friend email filed is not valid.");
            }
        }
    }
}
