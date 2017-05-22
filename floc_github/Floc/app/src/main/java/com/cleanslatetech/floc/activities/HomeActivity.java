package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.ChannelRecyclerAdapter;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocRecyclerAdapter;
import com.cleanslatetech.floc.asynctask.GetMyProfile;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.interfaces.InterfaceAllRecent_Current_Archive_Event;
import com.cleanslatetech.floc.utilities.EnumFlocDescFrom;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HomeActivity extends BaseAppCompactActivity implements InterfaceAllRecent_Current_Archive_Event {

    ViewPager mViewPager;
    private CustomSliderPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private int delay = 2500; //milliseconds
    private int page = 0;
    private Handler handler;
    Runnable runnable = new Runnable() {
        public void run() {
            if (mAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            mViewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    public static JSONArray jsonArrayAllArchive, jsonArrayAllEvents, jsonArrayAllRecent, jsonArrayAllChannel;
    public static InterfaceAllRecent_Current_Archive_Event interfaceAllRecentAndCurrentEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        jsonArrayAllArchive = new JSONArray();
        jsonArrayAllEvents = new JSONArray();
        jsonArrayAllRecent = new JSONArray();
        jsonArrayAllChannel = new JSONArray();

        interfaceAllRecentAndCurrentEvent = this;

        new GetMyProfile(this).getData();
    }

    private void setSlideOrInterestGrid() {
        int[] mResources = new int[]{R.drawable.home_slider_1, R.drawable.home_slider_2, R.drawable.home_slider_3, R.drawable.home_slider_4};

        handler = new Handler();
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

        handler.postDelayed(runnable, delay);

        setPageViewIndicator();

    }

    @Override
    public void onBackPressed() {

        TextView textView = new TextView(HomeActivity.this);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setTextSize(17);
        textView.setLineSpacing(2, 1);
        textView.setText("Thanks for visiting! We're fine tuning your flocworld experience every time you drop by. See you soon!");

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setCancelable(false)
                .setView(layout)
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
        if (handler != null) {
            handler.postDelayed(runnable, delay);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
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
    public void getAllChannle(JSONArray jsonArray) {
        jsonArrayAllChannel = jsonArray;
    }

    @Override
    public void getAllArchive(JSONArray paramJsonArray) {
        Uri URIdata = getIntent().getData();
        if( URIdata != null ) {
            List params = URIdata.getPathSegments();
            String param3 = params.get(2).toString();
            System.out.println(param3);
        }

        setContentView(R.layout.activity_home);
        super.setToolBar("Home");

        setSlideOrInterestGrid();

        jsonArrayAllArchive = paramJsonArray;

        GetSharedPreference getSharedPreference = new GetSharedPreference(HomeActivity.this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.id_home_selected_events);
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);

        tvBtnMoreEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllEventActivity.class));
            }
        });

        Set<String> strIntArrayInterestCategory = getSharedPreference.getStringSet(getResources().getString(R.string.shrdSelectedCategory));
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

                if (strIntArrayInterestCategory.contains(eventCategoryId) ) {

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
            View inflate = mInflater.inflate(R.layout.single_floc_layout, null, false);
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
            RecentFlocRecyclerAdapter mAdapter = new RecentFlocRecyclerAdapter(this, hmapInterest.get(key), EnumFlocDescFrom.Home.toString());
            recyclerView.setAdapter(mAdapter);

            ll.addView(inflate);
            ll.addView(recyclerView);
        }

        RecyclerView llChannel = (RecyclerView) findViewById(R.id.id_home_channel_layout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        llChannel.setLayoutManager(mLayoutManager);

        // Initialize a new Adapter for RecyclerView
        ChannelRecyclerAdapter adapterRecent = new ChannelRecyclerAdapter(this, jsonArrayAllChannel);
        llChannel.setAdapter(adapterRecent);
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
