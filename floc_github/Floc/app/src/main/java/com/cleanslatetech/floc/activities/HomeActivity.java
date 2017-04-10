package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;
import com.cleanslatetech.floc.asynctask.GetMyProfile;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.interfaces.InterfaceAllRecent_Current_Archive_Event;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class HomeActivity extends BaseAppCompactActivity implements InterfaceAllRecent_Current_Archive_Event {

    ViewPager mViewPager;
    private CustomSliderPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    public static JSONArray jsonArrayAllArchive, jsonArrayAllEvents, jsonArrayAllRecent;
    public static InterfaceAllRecent_Current_Archive_Event interfaceAllRecentAndCurrentEvent;

    private GetSharedPreference getSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        jsonArrayAllArchive = new JSONArray();
        jsonArrayAllEvents = new JSONArray();
        jsonArrayAllRecent = new JSONArray();

        interfaceAllRecentAndCurrentEvent = this;

        new GetMyProfile(this).getData();
    }

    private void setSlideOrInterestGrid() {
        int[] mResources = new int[]{R.drawable.slider_1, R.drawable.slider_3, R.drawable.slider_4, R.drawable.slider_5};

        mViewPager = (ViewPager) findViewById(R.id.slider_viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new CustomSliderPagerAdapter(this, mResources);
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
        jsonArrayAllRecent = jsonArray;
    }

    @Override
    public void getAllArchive(JSONArray paramJsonArray) {
        setContentView(R.layout.activity_home);
        super.setToolBar("Home");

        setSlideOrInterestGrid();

        jsonArrayAllArchive = paramJsonArray;

//        initRecentFlocGridview();
//        initEventGridview();
        getSharedPreference = new GetSharedPreference(HomeActivity.this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.id_home_selected_events);
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);

        tvBtnMoreEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllEventActivity.class));
            }
        });

        String strIntArrayInterestCategory = getSharedPreference.getString(getResources().getString(R.string.shrdSelectedCategory));
        String strIntArrayAllCategory = getSharedPreference.getString(getResources().getString(R.string.shrdAllCategoryList));
        JSONArray joAllCategory = null;

        try {
            joAllCategory = new JSONArray(strIntArrayAllCategory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonArrayAllEvents.length() <= 0) {
            findViewById(R.id.id_home_event_panel).setVisibility(View.GONE);
            return;
        }

        HashMap<Integer, JSONArray> hmapInterest = new HashMap<Integer, JSONArray>();

        for( int j = 0 ; j < jsonArrayAllEvents.length() ; j++  ) {
            try {
                int eventCategoryId = jsonArrayAllEvents.getJSONObject(j).getInt("EventCategory");

                JSONArray jsonArray = new JSONArray();

                if (strIntArrayInterestCategory.contains(""+eventCategoryId) ) {

                    boolean isEventCategory = hmapInterest.containsKey(eventCategoryId);

                    if(isEventCategory) {
                        jsonArray = hmapInterest.get(eventCategoryId);
                    }

                    jsonArray.put(jsonArrayAllEvents.getJSONObject(j));
                    hmapInterest.put(eventCategoryId, jsonArray);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for ( Integer key : hmapInterest.keySet() ) {
            String categoryName = "";

            for (int k = 0; k < joAllCategory.length(); k ++) {
                try {
                    if(key == joAllCategory.getJSONObject(k).getInt("EventCategoryId")) {
                        categoryName = joAllCategory.getJSONObject(k).getString("EventCategoryName");
                        joAllCategory.remove(k);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflate = mInflater.inflate(R.layout.category_text_layout, null, false);
            AppCompatTextView tvCategory = (AppCompatTextView) inflate.findViewById(R.id.id_dynamic_category_name);
            tvCategory.setText(categoryName);

            RecyclerView recyclerView = new RecyclerView(this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                    this,
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            recyclerView.setLayoutManager(mLayoutManager);
            // Initialize a new Adapter for RecyclerView
            RecentFlocRecyclerAdapter mAdapter = new RecentFlocRecyclerAdapter(this, hmapInterest.get(key));
            recyclerView.setAdapter(mAdapter);

            ll.addView(inflate);
            ll.addView(recyclerView);
        }
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
