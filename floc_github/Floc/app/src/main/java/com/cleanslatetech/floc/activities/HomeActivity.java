package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.EventFlocAdapter;
import com.cleanslatetech.floc.adapter.InterestAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class HomeActivity extends BaseAppCompactActivity {

    private LinearLayout linearLayoutSelectInterest;
    private SlidingSplashView sliderLayout;
    private AppCompatTextView tvBtnSaveInterest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        super.setToolBar("");

        setSlideOrInterestGrid();

        initRecentFlocGridview();
        initEventGridview();

        ((AppCompatTextView)findViewById(R.id.onClickCreateFloc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CreateFlocActivity.class));
            }
        });
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
        final InterestAdapter adapterInterest = new InterestAdapter(this);
        gridview.setAdapter(adapterInterest);

        tvBtnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(adapterInterest.iArraySelectedPositions);
                new SetSharedPreference(HomeActivity.this).setBoolean(getResources().getString(R.string.shrdIsInterestSelected),true);
                sliderLayout.setVisibility(View.VISIBLE);
                linearLayoutSelectInterest.setVisibility(View.GONE);
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
