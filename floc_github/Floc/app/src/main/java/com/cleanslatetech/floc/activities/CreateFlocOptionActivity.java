package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;

public class CreateFlocOptionActivity extends BaseAppCompactActivity {
    private AppCompatTextView tvSelectedCreateFlocOption;
    private ViewPager mViewPager;
    private LinearLayout pager_indicator;
    int _dotsCount;
    ImageView[] _dots;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc_option);

        super.setToolBar(getResources().getString(R.string.build_a_floc));

        tvSelectedCreateFlocOption = (AppCompatTextView) findViewById(R.id.id_selected_build_floc_option);

        int[] mResources = new int[]{R.drawable.create_floc_1, R.drawable.create_floc_2, R.drawable.create_floc_3, R.drawable.create_floc_4};

        mViewPager = (ViewPager) findViewById(R.id.create_intro_slider_viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.create_intro_count_dots);

        populateSlider(mResources);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickShowDetails_BuildFloc(View view) {
        String desc = "<b>Build a floc</b> refers to your creating a community or group of friends, contacts or targeted audiences to attend or visit an event, experiential platform or online storefront.";
        showDialog(getResources().getString(R.string.build_a_floc), desc);

        int[] mResources = new int[]{R.drawable.create_floc_1, R.drawable.create_floc_2, R.drawable.create_floc_3, R.drawable.create_floc_4};

        populateSlider(mResources);

        tvSelectedCreateFlocOption.setText(getResources().getString(R.string.build_a_floc));
    }

    public void onClickShowDetails_BuildPlatform(View view) {
        String desc = "<b>Build a platform</b> refers to the creation of an event, experiential or commercial platform that will enable organisers of the platform to invite guests, customers or visitors as their flocs.";
        showDialog(getResources().getString(R.string.build_a_platform), desc);

        int[] mResources = new int[]{R.drawable.create_platform_1};

        populateSlider(mResources);

        tvSelectedCreateFlocOption.setText(getResources().getString(R.string.build_a_platform));
    }

    private void populateSlider(int[] mResources) {
        CustomSliderPagerAdapter mAdapter = new CustomSliderPagerAdapter(this, mResources);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        _dotsCount = mAdapter.getCount();
        _dots = new ImageView[_dotsCount];

        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < _dotsCount; i++) {
                    _dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                _dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager_indicator.removeAllViews();
        pager_indicator.invalidate();
        System.out.println("_dotsCount: "+ _dotsCount);

        for (int i = 0; i < _dotsCount; i++) {
            _dots[i] = new ImageView(this);
            _dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(20, 0, 20, 0);

            final int presentPosition = i;
            _dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });

            pager_indicator.addView(_dots[i], params);
        }

        _dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    private void setPageViewIndicator() {

    }

    public void showDialog (String title, String message) {
        TextView textView = new TextView(CreateFlocOptionActivity.this);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setTextSize(15);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(message));
        }

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 10, 30, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(textView);

        new AlertDialog.Builder(CreateFlocOptionActivity.this)
                .setTitle(title)
                .setView(layout)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public void onClickGoToBuildFloc(View view) {
        startActivity(new Intent(CreateFlocOptionActivity.this, CreateFlocActivity.class));
        finish();
    }

    public void onClickGoToBuildPlatform(View view) {
        startActivity(new Intent(CreateFlocOptionActivity.this, CreatePlatformActivity.class));
        finish();
    }
}
