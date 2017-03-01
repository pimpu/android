package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.EventFlocAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;
import com.cleanslatetech.floc.asynctask.GetInterestCategoryAsyncTask;
import com.cleanslatetech.floc.asynctask.SetInterestAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;

import java.io.File;

public class HomeActivity extends BaseAppCompactActivity {

    private AppCompatTextView tvBtnSaveInterest;
    SlidingSplashView sliderLayout;
    LinearLayout linearLayoutSelectInterest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        super.setToolBar("");

        setSlideOrInterestGrid();

        initRecentFlocGridview();
        initEventGridview();
    }

    private void setSlideOrInterestGrid() {

        linearLayoutSelectInterest = (LinearLayout) findViewById(R.id.selectIntrest_layout);
        sliderLayout = (SlidingSplashView) findViewById(R.id.slider_layout);

        boolean isAvailInterest = new GetSharedPreference(this).getBoolean(getResources().getString(R.string.shrdIsInterestSelected));

        if( !isAvailInterest ) {
            sliderLayout.setVisibility(View.GONE);
            linearLayoutSelectInterest.setVisibility(View.VISIBLE);
            interestGridview();
        }

    }

    private void interestGridview() {
        tvBtnSaveInterest = (AppCompatTextView) findViewById(R.id.tvBtnSaveInterest);
        GridView gridview = (GridView) findViewById(R.id.gridviewInterest);

        final GetInterestCategoryAsyncTask getInterestCategoryAsyncTask = new GetInterestCategoryAsyncTask(HomeActivity.this, gridview);

        tvBtnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetInterestAsyncTask(
                        HomeActivity.this,
                        getInterestCategoryAsyncTask.getSelectedCategoryArray(),
                        sliderLayout,
                        linearLayoutSelectInterest).postData();
            }
        });
    }

    public void changeState_saveInterest(int counter) {
        if(counter == 0) {
            tvBtnSaveInterest.setVisibility(View.VISIBLE);
        }
        else {
            tvBtnSaveInterest.setVisibility(View.GONE);
        }
    }

    public void initRecentFlocGridview() {
        AppCompatTextView tvBtnMoreRecentFloc = (AppCompatTextView) findViewById(R.id.tvBtnMoreRecentFloc);
        GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewRecentFloc);
        RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this);
        gridviewRecentFloc.setAdapter(adapterRecent);
    }

     public void initEventGridview() {
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);
        GridView gridviewEvent = (GridView) findViewById(R.id.gridviewEvent);
        EventFlocAdapter adapterRecent = new EventFlocAdapter(this);
        gridviewEvent.setAdapter(adapterRecent);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setCancelable(false)
                .setMessage("Do you want to Exit?")
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
