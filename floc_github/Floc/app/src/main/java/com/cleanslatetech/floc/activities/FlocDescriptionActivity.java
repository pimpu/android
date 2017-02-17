package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONException;
import org.json.JSONObject;

public class FlocDescriptionActivity extends AppCompatActivity {
    AppCompatImageView imgFlocPic;
    private int screenHeight;
    private int toolbarHeight_org;
    private int toolbarHeight;
    private int linearLayoutHeight;
    private final Context mContext = this;
    AppCompatTextView name, details, category, startDate, startTime, endDate, endTime, member, price, address,
                        city, state, country, url, reason, publish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_description);

        toolbarSetup();

        init();
    }

    public void toolbarSetup() {

        screenHeight = getScreenHeight(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_floc_desc);
//        toolbar.setTitle("Floc Description");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        final CoordinatorLayout.LayoutParams appbarLayoutParams = (CoordinatorLayout.LayoutParams)appbar.getLayoutParams();

        final ViewGroup.LayoutParams toolbarLayoutParams = toolbar.getLayoutParams();
        if (toolbarLayoutParams != null) {
            toolbarHeight_org = toolbarLayoutParams.height;
            toolbarHeight = toolbarLayoutParams.height;
        }

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Floc Description");
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.shadowText);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_floc_desc);
        ViewTreeObserver observer = linearLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayoutHeight = linearLayout.getHeight();
                if (linearLayoutHeight + toolbarHeight < screenHeight) {
                    if (toolbarLayoutParams != null) {
                        toolbarLayoutParams.height = screenHeight - linearLayoutHeight - 55;
                        if (toolbarLayoutParams.height < toolbarHeight_org) {
                            toolbarLayoutParams.height = toolbarHeight_org;
                        }

                        int extended_text_size = (int) getResources().getDimension(R.dimen.grid_column_textsize);

                        if (appbarLayoutParams.height - toolbarLayoutParams.height <= extended_text_size) {
                            int value = appbarLayoutParams.height - toolbarLayoutParams.height;
                            if (value < 0) {
                                appbarLayoutParams.height = toolbarLayoutParams.height - value + extended_text_size * 3;
                            } else {
                                appbarLayoutParams.height = toolbarLayoutParams.height + extended_text_size * 3;
                            }
                            if (appbarLayoutParams.height >= screenHeight) {
                                appbarLayoutParams.height = screenHeight;
                            }
                        }

                        // collapsingToolbar.setContentScrimColor(getResources().getColor(android.R.color.transparent));
                        if (toolbarLayoutParams.height > toolbarHeight_org) {
                            collapsingToolbar.setContentScrimColor(ContextCompat.getColor(mContext, android.R.color.transparent));
                        }
                    }
                }
                // Removes the listener if possible
                ViewTreeObserver viewTreeObserver = linearLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        appbar.setExpanded(true);

    }

    private int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }

        return measuredHeight;
    }

    private void init() {
        String intent_floc_data = getIntent().getExtras().getString("floc_data");

        name = (AppCompatTextView) findViewById(R.id.floc_description_name);
        details = (AppCompatTextView) findViewById(R.id.floc_description_details);
        category = (AppCompatTextView) findViewById(R.id.floc_description_category);
        startDate = (AppCompatTextView) findViewById(R.id.floc_description_start_date);
        startTime = (AppCompatTextView) findViewById(R.id.floc_description_start_time);
        endDate = (AppCompatTextView) findViewById(R.id.floc_description_end_date);
        endTime = (AppCompatTextView) findViewById(R.id.floc_description_end_time);
        member = (AppCompatTextView) findViewById(R.id.floc_description_member);
        price = (AppCompatTextView) findViewById(R.id.floc_description_price);
        address = (AppCompatTextView) findViewById(R.id.floc_description_address_area);
        city = (AppCompatTextView) findViewById(R.id.floc_description_city);
        state = (AppCompatTextView) findViewById(R.id.floc_description_state);
        country = (AppCompatTextView) findViewById(R.id.floc_description_country);
        url = (AppCompatTextView) findViewById(R.id.floc_description_url);
        reason = (AppCompatTextView) findViewById(R.id.floc_description_reason);
        publish = (AppCompatTextView) findViewById(R.id.floc_description_publish);

        imgFlocPic = (AppCompatImageView) findViewById(R.id.floc_description_image);

        try {
            JSONObject jsonFlocData = new JSONObject(intent_floc_data);
            Glide
                    .with(this)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + jsonFlocData.getString("EventPicture"))
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(imgFlocPic);

            name.setText(jsonFlocData.getString("EventName"));
            details.setText("Description: \n"+jsonFlocData.getString("EventDescription"));
            category.setText("Category: \n"+jsonFlocData.getString("EventCategory"));
            startDate.setText("Start Date: \n"+jsonFlocData.getString("EventStartDate"));
            startTime.setText("Start Time: \n"+jsonFlocData.getString("EventStartHour")+":"+jsonFlocData.getString("EventStartMin"));
            endDate.setText("End Date: \n"+jsonFlocData.getString("EventEndDate"));
            endTime.setText("End Time:\n"+jsonFlocData.getString("EventEndHour")+":"+jsonFlocData.getString("EventEndMin"));
            member.setText("Member:\n"+jsonFlocData.getString("EventMembers"));
            price.setText("Price: "+jsonFlocData.getString("EventPrice"));
            address.setText("Address:\n"+jsonFlocData.getString("EventAddress")+", "+jsonFlocData.getString("EventArea"));
            city.setText("City:\n"+jsonFlocData.getString("EventCity"));
            state.setText("State:\n"+jsonFlocData.getString("EventState"));
            country.setText("Country:\n"+jsonFlocData.getString("EventCountry"));
            url.setText("Url:\n"+jsonFlocData.getString("EventUrl"));
            reason.setText("Reason:\n"+jsonFlocData.getString("EventReason"));
            publish.setText("Publish:\n"+jsonFlocData.getString("EventPublish"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
