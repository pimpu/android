package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;

public class CreateFlocIntroSliderActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private LinearLayout pager_indicator;
    int _dotsCount;
    ImageView[] _dots;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc_intro_slider);

        from = getIntent().getExtras().getString("strFrom");
        int[] mResources;

        if(from.equals("floc")) {
            mResources = new int[]{R.drawable.create_floc_1, R.drawable.create_floc_2, R.drawable.create_floc_3, R.drawable.create_floc_4};

        } else {
            mResources = new int[]{R.drawable.create_platform_1};
        }

        System.out.println(mResources.length);
        mViewPager = (ViewPager) findViewById(R.id.create_intro_slider_viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.create_intro_count_dots);

        populateSlider(mResources);
    }

    private void populateSlider(int[] mResources) {
        CustomSliderPagerAdapter mAdapter = new CustomSliderPagerAdapter(this, mResources);
        mViewPager.setAdapter(mAdapter);

        _dotsCount = mAdapter.getCount();
        _dots = new ImageView[_dotsCount];

        if (_dotsCount == 1) {
            findViewById(R.id.skipCreateFloCIntro).setVisibility(View.GONE);
            findViewById(R.id.startBuildCreateFloCIntro).setVisibility(View.VISIBLE);
        }

        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (_dotsCount-1)) {
                    findViewById(R.id.skipCreateFloCIntro).setVisibility(View.GONE);
                    findViewById(R.id.startBuildCreateFloCIntro).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.startBuildCreateFloCIntro).setVisibility(View.GONE);
                    findViewById(R.id.skipCreateFloCIntro).setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < _dotsCount; i++) {
                    _dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                _dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    public void gotoCreateForm(View view) {
        if(from.equals("floc")) {
            startActivity(new Intent(CreateFlocIntroSliderActivity.this, CreateFlocActivity.class));
            finish();

        } else {
            startActivity(new Intent(CreateFlocIntroSliderActivity.this, CreatePlatformActivity.class));
            finish();
        }
    }
}
