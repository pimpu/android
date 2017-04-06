package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;
import com.cleanslatetech.floc.asynctask.GetAllEventsAsyncTask;
import com.cleanslatetech.floc.asynctask.GetInterestCategoryAsyncTask;
import com.cleanslatetech.floc.asynctask.GetMyProfile;
import com.cleanslatetech.floc.asynctask.SetInterestAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.interfaces.InterfaceAllRecentAndCurrentEvent;
import com.cleanslatetech.floc.utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseAppCompactActivity implements InterfaceAllRecentAndCurrentEvent {

    ViewPager mViewPager;
    private CustomSliderPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    public static JSONArray jsonArrayAllRecent, jsonArrayAllEvents;
    public static InterfaceAllRecentAndCurrentEvent interfaceAllRecentAndCurrentEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        jsonArrayAllRecent = new JSONArray();
        jsonArrayAllEvents = new JSONArray();

        interfaceAllRecentAndCurrentEvent = this;

        new GetMyProfile(this).getData();
    }

    private void setSlideOrInterestGrid() {

        mViewPager = (ViewPager) findViewById(R.id.slider_viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new CustomSliderPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setPageViewIndicator();

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

    @Override
    protected void onResume() {
        super.onResume();
        createRightPopupMenu();
    }

    @Override
    public void getAllEvents(JSONArray jsonArray) {
        jsonArrayAllEvents = jsonArray;
    }

    @Override
    public void getAllRecent(JSONArray jsonArray) {
        setContentView(R.layout.activity_home);
        super.setToolBar("Home");

        setSlideOrInterestGrid();

        jsonArrayAllRecent = jsonArray;

        initRecentFlocGridview();
        initEventGridview();
    }

    public void initEventGridview() {
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);
        GridView gridviewEvent = (GridView) findViewById(R.id.gridviewEvent);
        JSONArray jsonArrayLatestEvent;
        if(jsonArrayAllEvents.length() > 0) {

            jsonArrayLatestEvent = new JSONArray();

            if(jsonArrayAllEvents.length() > 4) {

                for(int i = 1 ; i < 5 ; i++ ) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = jsonArrayAllEvents.getJSONObject( jsonArrayAllEvents.length()-i );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArrayLatestEvent.put(jsonObject);
                }
            }
            else {
                jsonArrayLatestEvent = jsonArrayAllEvents;
            }

            RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this, jsonArrayLatestEvent);
            gridviewEvent.setAdapter(adapterRecent);
            final JSONArray finalJsonArrayLatestEvent = jsonArrayLatestEvent;
            gridviewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intentFlocDesc = new Intent(HomeActivity.this, FlocDescriptionActivity.class);
                        intentFlocDesc.putExtra("floc_data", finalJsonArrayLatestEvent.getJSONObject(position).toString());
                        intentFlocDesc.putExtra("from", "Event");
                        startActivity(intentFlocDesc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            tvBtnMoreEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedCategory = new GetSharedPreference(HomeActivity.this).getString(getResources().getString(R.string.shrdSelectedCategory));
                    if(selectedCategory == null) {
                        CommonUtilities.customToast(HomeActivity.this, "Please, Select Interest");
                    }
                    else {
                        startActivity(new Intent(HomeActivity.this, AllEventActivity.class));
                    }
                }
            });

        } else {
            findViewById(R.id.id_home_event_panel).setVisibility(View.GONE);
        }

    }

    public void initRecentFlocGridview() {
        AppCompatTextView tvBtnMoreRecentFloc = (AppCompatTextView) findViewById(R.id.tvBtnMoreRecentFloc);
        GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewRecentFloc);

        JSONArray jsonArrayLatestRecent = new JSONArray();

        if( jsonArrayAllRecent.length() > 4) {

            for(int i = 1 ; i < 5 ; i++ ) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = jsonArrayAllRecent.getJSONObject( jsonArrayAllRecent.length()-i );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArrayLatestRecent.put(jsonObject);
            }
        }
        else {
            jsonArrayLatestRecent = jsonArrayAllRecent;
        }

        RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this, jsonArrayLatestRecent);
        gridviewRecentFloc.setAdapter(adapterRecent);

        final JSONArray finalJsonArrayLatestRecent = jsonArrayLatestRecent;
        gridviewRecentFloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                try {
                    Intent intentFlocDesc = new Intent(HomeActivity.this, FlocDescriptionActivity.class);
                    intentFlocDesc.putExtra("floc_data", finalJsonArrayLatestRecent.getJSONObject(position).toString());
                    intentFlocDesc.putExtra("from", "RecentFloc");
                    startActivity(intentFlocDesc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tvBtnMoreRecentFloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = new GetSharedPreference(HomeActivity.this).getString(getResources().getString(R.string.shrdSelectedCategory));
                if(selectedCategory == null) {
                    CommonUtilities.customToast(HomeActivity.this, "Please, Select Interest");
                }
                else {
                    startActivity(new Intent(HomeActivity.this, AllRecentEventActivity.class));
                }
            }
        });
    }

    private void setPageViewIndicator() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(20, 0, 20, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }
}
