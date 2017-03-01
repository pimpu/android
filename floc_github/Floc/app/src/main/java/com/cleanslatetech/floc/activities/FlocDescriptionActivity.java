package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.FacebookCallBackMethod;
import com.cleanslatetech.floc.utilities.MakeTextResizable;
import com.cleanslatetech.floc.utilities.MySpannable;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;

import org.json.JSONException;
import org.json.JSONObject;

public class FlocDescriptionActivity extends BaseAppCompactActivity {
    private String URL = null;
    AppCompatImageView imgFlocPic;
    AppCompatTextView tvDetails;

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private PlusOneButton mPlusOneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_description);

        super.setToolBar("Floc Description");

        init();
    }

    private void init() {
        String eventPicture = null;
        String intent_floc_data = getIntent().getExtras().getString("floc_data");
        imgFlocPic = (AppCompatImageView) findViewById(R.id.img_floc_desc);
        tvDetails = (AppCompatTextView) findViewById(R.id.floc_description_details);

        try {
            JSONObject jsonFlocData = new JSONObject(intent_floc_data);
            eventPicture = jsonFlocData.getString("EventPicture");

            tvDetails.setText("Description: "+jsonFlocData.getString("EventDescription"));
            MakeTextResizable.makeTextViewResizable(tvDetails, 3, "See More", true);

            Glide
                    .with(this)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(imgFlocPic);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // facebook share
        ShareButton shareButton = (ShareButton) findViewById(R.id.shareButton);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture))
                .build();
        shareButton.setShareContent(content);

        // Google+ share
        final String finalEventPicture = eventPicture;

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        URL = CommonVariables.EVENT_IMAGE_SERVER_URL + finalEventPicture;
       /* mPlusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(FlocDescriptionActivity.this)
                        .setType("text/plain")
                        .setText("Welcome to the Google+ platform.")
                        .setContentUrl(Uri.parse(CommonVariables.EVENT_IMAGE_SERVER_URL + finalEventPicture))
                        .getIntent();
                startActivityForResult(shareIntent, 0);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
