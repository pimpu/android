package com.cleanslatetech.floc.activities;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.FacebookCallBackMethod;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleGoogleSignInResult;
import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignIn;
import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class FeaturingActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private CustomSliderPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featuring);

        int[] mResources = new int[]{R.drawable.feature_slider_1, R.drawable.feature_slider_2,
                R.drawable.feature_slider_3, R.drawable.feature_slider_4, R.drawable.feature_slider_5};

        mViewPager = (ViewPager) findViewById(R.id.features_viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.features_viewPagerCountDots);
        mAdapter = new CustomSliderPagerAdapter(this, mResources);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (dotsCount-1)) {
                    findViewById(R.id.id_skip_feature).setVisibility(View.GONE);
                    findViewById(R.id.id_proceed_to_app).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.id_proceed_to_app).setVisibility(View.GONE);
                    findViewById(R.id.id_skip_feature).setVisibility(View.VISIBLE);
                }

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

    public void gotoNext(View view) {
        /*new SetSharedPreference(this).setBoolean(getResources().getString(R.string.isShowFeatureActivity), true);
        handleIntentWhenSignOut(this);*/

        GetSharedPreference getSharedPreference = new GetSharedPreference(FeaturingActivity.this);

        boolean isSignIn = getSharedPreference.getBoolean(getResources().getString(R.string.isAppSignIn));

        if(isSignIn) {
            handleIntentWhenSignIn(
                    this,
                    getSharedPreference.getString(getResources().getString(R.string.shrdLoginType)),
                    getSharedPreference.getString(getResources().getString(R.string.shrdUserName)),
                    getSharedPreference.getString(getResources().getString(R.string.shrdUserEmail)),
                    getSharedPreference.getInt(getResources().getString(R.string.shrdLoginId)) );
        }
        else {
            // intet for next activity
            handleIntentWhenSignOut(this);
        }
    }
}
