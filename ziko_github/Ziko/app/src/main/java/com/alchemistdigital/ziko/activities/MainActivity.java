package com.alchemistdigital.ziko.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.alchemistdigital.ziko.R;


public class MainActivity extends AppCompatActivity {

    private static final int REQUESTCODE_PICK_VIDEO = 101;
    TabHost tabHost;
    TabHost.TabSpec setContentSpecs;
    String TAB_HOME="Home";
    String TAB_SEARCH="Search";
    String TAB_CAMERA="Camera";
    String TAB_ACTIVITY="Activity";
    String TAB_PROFILE="Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise and set tab content data
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        setupTab(TAB_HOME, R.id.Home, R.drawable.ic_home_white);
        setupTab(TAB_SEARCH, R.id.Search, R.drawable.ic_search_white);
        setupTab(TAB_CAMERA, R.id.Camera, R.drawable.ic_photo_camera_white);
        setupTab(TAB_ACTIVITY, R.id.Activity, R.drawable.ic_local_activity_white);
        setupTab(TAB_PROFILE, R.id.Profile, R.drawable.ic_person_white);

        // remove tabhost divider
        tabHost.getTabWidget().setDividerDrawable(null);

        tabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent startCameraActivity = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(startCameraActivity);
            }

        });
    }

    /**
     * creation of tab content
     * @param tabName
     * @param tabId
     * @param tabIcon
     */
    private void setupTab(String tabName, int tabId, int tabIcon) {
        View tabview = createTabView(tabHost.getContext(), tabIcon);
        setContentSpecs = tabHost.newTabSpec(tabName);
        setContentSpecs.setIndicator(tabview);
        setContentSpecs.setContent(tabId);
        tabHost.addTab(setContentSpecs);
    }

    /**
     * create drawable view of tab content
     * @param context
     * @param icon
     * @return
     */
    private static View createTabView(Context context, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.idTabImage);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(130, 130);
        //width and height of your Image ,if it is inside Relative change the LinearLayout to RelativeLayout.
        tabIcon.setLayoutParams(layoutParams);

        // set icon image to tab
        tabIcon.setImageDrawable(context.getResources().getDrawable(icon));
        return view;
    }
}
