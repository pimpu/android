package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.ChatRecyclerAdapter;
import com.cleanslatetech.floc.asynctask.BookEventAsyncTask;
import com.cleanslatetech.floc.asynctask.ChatAsyncTask;
import com.cleanslatetech.floc.asynctask.GetActivityAsyncTask;
import com.cleanslatetech.floc.asynctask.LikeStoreAsyncTask;
import com.cleanslatetech.floc.asynctask.RateEventAsyncTask;
import com.cleanslatetech.floc.asynctask.ReviewStoreAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.interfaces.InterfaceFlocDescTopics;
import com.cleanslatetech.floc.utilities.MakeTextResizable;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.plus.PlusOneButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;
import static com.cleanslatetech.floc.utilities.Validations.isEmptyString;

public class FlocDescriptionActivity extends BaseAppCompactActivity implements InterfaceFlocDescTopics {
    private AppCompatImageView imgFlocPic;
    private AppCompatRatingBar ratingBar;
    private AppCompatTextView tvDetails, tvAsyncText;
    private AppCompatEditText txtComment;
    private FloatingActionButton fabSentComment;

    public RecyclerView.Adapter commentRecyclerAdapter;
    private RecyclerView recyclerviewComments;
    private String URL = null, strUserEmail, strUserName, strEventName;

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private PlusOneButton mPlusOneButton;
    private RelativeLayout rlProgressLayout;
    private int iActivityIdRating, iActivityIdLike, iActivityIdReview;
    private int iEventId, iCategoryId, iCreaterId, iUSerId;

    public InterfaceFlocDescTopics interfaceFlocDescTopics;

    public static String strFrom;

    Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_description);

        super.setToolBar("Floc Description");

        init();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FlocDescriptionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getLatestEventUpdate();
                    }
                });
            }
        }, 0, 1000*10);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
    }

    @Override
    protected void onStop() {
        timer.cancel();
        timer.purge();
        super.onStop();
    }

    private void init() {
        interfaceFlocDescTopics = this;

        String eventPicture = null;
        String intent_floc_data = getIntent().getExtras().getString("floc_data");
        strFrom = getIntent().getExtras().getString("from");

        imgFlocPic = (AppCompatImageView) findViewById(R.id.img_floc_desc);

        rlProgressLayout = (RelativeLayout) findViewById(R.id.id_getactivity_progress_layout);

        tvDetails = (AppCompatTextView) findViewById(R.id.floc_description_details);
        tvAsyncText = (AppCompatTextView) findViewById(R.id.id_floc_desc_async_msg);

        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingBar);

        recyclerviewComments = (RecyclerView) findViewById(R.id.recyclerview_comments);

        fabSentComment = (FloatingActionButton) findViewById(R.id.id_fab_comment_send);

        txtComment = (AppCompatEditText) findViewById(R.id.id_txt_comment);
        // for aading vertical scrollbar inside EditText in parent ScrollView
        txtComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.id_txt_comment) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        // change background tint when text enter
        txtComment.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txtComment.getText().toString().length() > 0 ) {
                    fabSentComment.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_black));
                } else {
                    fabSentComment.setImageDrawable(getResources().getDrawable(R.drawable.ic_attach_file_black));
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        iUSerId = new GetSharedPreference(this).getInt(getResources().getString(R.string.shrdLoginId));
        strUserEmail = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail));
        strUserName = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserName));

        try {
            JSONObject jsonFlocData = new JSONObject(intent_floc_data);
            System.out.println(jsonFlocData);
            eventPicture = jsonFlocData.getString("EventPicture");
            iEventId = jsonFlocData.getInt("EventId");
            iCategoryId = jsonFlocData.getInt("EventCategory");
            iCreaterId = jsonFlocData.getInt("EventCreatorId");
            strEventName = jsonFlocData.getString("EventName");

            tvDetails.setText("Description: "+jsonFlocData.getString("EventDescription"));
            MakeTextResizable.makeTextViewResizable(tvDetails, 3, "See More", true);

            Glide
                    .with(this)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(imgFlocPic);


            try {
                JSONObject jsonObject = new JSONObject(
                        new GetSharedPreference(this).getString(
                                getResources().getString(R.string.shrdActivityId)) );

                iActivityIdRating = jsonObject.getInt("rate");
                iActivityIdLike = jsonObject.getInt("like");
                iActivityIdReview = jsonObject.getInt("review");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // get latest event data
//            getLatestEventUpdate();

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
//        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        URL = CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture;

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getLatestEventUpdate() {
        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {
            CommonUtilities.customToast(FlocDescriptionActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        }
        else {
            new GetActivityAsyncTask(this, iEventId, tvAsyncText, iUSerId).getData();
        }

    }

    public void onClickLikeEvent(View view) {
        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {
            new LikeStoreAsyncTask(FlocDescriptionActivity.this, tvAsyncText, iActivityIdLike, iEventId, iUSerId).postData();
        }
    }

    public void OnClickSubmitReview(View view) {
        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {
            AppCompatEditText txtReview = (AppCompatEditText) findViewById(R.id.id_txt_review);

            boolean boolReview = isEmptyString(txtReview.getText().toString());

            if(boolReview) {
                new ReviewStoreAsyncTask(FlocDescriptionActivity.this, tvAsyncText, iActivityIdReview,
                        iEventId, iUSerId, txtReview.getText().toString()).postData();
            }
            else {
                CommonUtilities.customToast(FlocDescriptionActivity.this, "Review field is empty.");
            }

        }
    }

    public void onClickBookEvent(View view) {
        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {

            new BookEventAsyncTask(FlocDescriptionActivity.this, tvAsyncText,
                    iUSerId, iEventId, iCategoryId, strUserName, strUserEmail, 1).postData();
        }
    }

    public void onClickSubmitReview(View view) {
        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {

            new RateEventAsyncTask(FlocDescriptionActivity.this, tvAsyncText,
                    iActivityIdRating, iUSerId, iEventId, iCreaterId, Math.round(ratingBar.getRating()) ).postData();
        }
    }

    @Override
    public void onClickLike(JSONArray jsonArrayAllLike) {
        Intent intent = new Intent(FlocDescriptionActivity.this, FlocDescTopicsActivity.class);
        intent.putExtra("flocTopics", jsonArrayAllLike.toString());
        intent.putExtra("what", "likes");
        startActivity(intent);
    }

    @Override
    public void onClickReview(JSONArray jsonArrayAllReviews) {
        Intent intent = new Intent(FlocDescriptionActivity.this, FlocDescTopicsActivity.class);
        intent.putExtra("flocTopics", jsonArrayAllReviews.toString());
        intent.putExtra("what", "reviews");
        startActivity(intent);
    }

    @Override
    public void onClickBooking(JSONArray jsonArrayAllBooking) {
        Intent intent = new Intent(FlocDescriptionActivity.this, FlocDescTopicsActivity.class);
        intent.putExtra("flocTopics", jsonArrayAllBooking.toString());
        intent.putExtra("what", "booking");
        startActivity(intent);
    }

    @Override
    public void getChatData(JSONArray jsonArrayAllChats) {

        commentRecyclerAdapter = new ChatRecyclerAdapter(this, jsonArrayAllChats, iUSerId);
        recyclerviewComments.setAdapter(commentRecyclerAdapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerviewComments.setLayoutManager(layout);
        layout.scrollToPosition(jsonArrayAllChats.length()-1);
    }

    public void onClickSentComment(View view) {
        String strComment = txtComment.getText().toString();

        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {
            if(strComment.length() > 0) {
                new ChatAsyncTask(FlocDescriptionActivity.this, tvAsyncText,
                        iUSerId, iEventId, strEventName, strComment ).postData();
            }
        }
    }
}
