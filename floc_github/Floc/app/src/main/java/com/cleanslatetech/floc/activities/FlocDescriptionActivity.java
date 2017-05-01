package com.cleanslatetech.floc.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.ChatRecyclerAdapter;
import com.cleanslatetech.floc.asynctask.BookEventAsyncTask;
import com.cleanslatetech.floc.asynctask.ChangePwdAsyncTask;
import com.cleanslatetech.floc.asynctask.ChatAsyncTask;
import com.cleanslatetech.floc.asynctask.EventInvitationAsyncTask;
import com.cleanslatetech.floc.asynctask.GetActivityAsyncTask;
import com.cleanslatetech.floc.asynctask.LikeStoreAsyncTask;
import com.cleanslatetech.floc.asynctask.PostRecentVisitedEventAsyncTask;
import com.cleanslatetech.floc.asynctask.RateEventAsyncTask;
import com.cleanslatetech.floc.asynctask.ReviewStoreAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.interfaces.InterfaceFlocDescTopics;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.EnumFlocDescFrom;
import com.cleanslatetech.floc.utilities.MakeTextResizable;
import com.cleanslatetech.floc.utilities.Validations;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;
import static com.cleanslatetech.floc.utilities.Validations.isEmptyString;

public class FlocDescriptionActivity extends BaseAppCompactActivity implements InterfaceFlocDescTopics {
    private AppCompatImageView imgFlocPic;
    private AppCompatRatingBar ratingBar;
    private AppCompatTextView tvDetails, tvAsyncText, tvStartDate, tvStartTime, tvEndDate, tvEndTime, tvAddress;
    private AppCompatEditText txtComment;
    private FloatingActionButton fabSentComment;
    private PopupWindow popupWindow;

    public RecyclerView.Adapter commentRecyclerAdapter;
    private RecyclerView recyclerviewComments;
    private String URL = null, strUserEmail, strUserName, strEventName;

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private ShareButton shareButton;
    private RelativeLayout rlProgressLayout;
    private int iActivityIdRating, iActivityIdLike, iActivityIdReview;
    private int iEventId, iCategoryId, iCreaterId, iUSerId;

    public InterfaceFlocDescTopics interfaceFlocDescTopics;

    public static String strFrom;

    Timer timer;
    GetSharedPreference getSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_description);

        super.setToolBar("Floc Description");

        getSharedPreference = new GetSharedPreference(this);

        init();

        // register this event into recently visited
        if (isConnectingToInternet(this)) {
            new PostRecentVisitedEventAsyncTask(this, iUSerId, iEventId, iCategoryId).postData();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FlocDescriptionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnectingToInternet(FlocDescriptionActivity.this)) {
                            CommonUtilities.customToast(FlocDescriptionActivity.this, getResources().getString(R.string.strNoInternet));
                            // stop executing code by return
                            return;
                        }
                        else {
                            new GetActivityAsyncTask(FlocDescriptionActivity.this, iEventId, iUSerId).getData();
                        }
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

        iUSerId = getSharedPreference.getInt(getResources().getString(R.string.shrdLoginId));
        strUserEmail = getSharedPreference.getString(getResources().getString(R.string.shrdUserEmail));
        strUserName = getSharedPreference.getString(getResources().getString(R.string.shrdUserName));

        imgFlocPic = (AppCompatImageView) findViewById(R.id.img_floc_desc);

        rlProgressLayout = (RelativeLayout) findViewById(R.id.id_getactivity_progress_layout);

        tvDetails = (AppCompatTextView) findViewById(R.id.floc_description_details);
        tvStartDate = (AppCompatTextView) findViewById(R.id.id_flocdesc_startDate);
        tvStartTime = (AppCompatTextView) findViewById(R.id.id_flocdesc_startTime);
        tvEndDate = (AppCompatTextView) findViewById(R.id.id_flocdesc_endDate);
        tvEndTime = (AppCompatTextView) findViewById(R.id.id_flocdesc_endTime);
        tvAddress = (AppCompatTextView) findViewById(R.id.floc_description_address);
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

        try {
            JSONObject jsonFlocData = new JSONObject(intent_floc_data);
            System.out.println(jsonFlocData);
            eventPicture = jsonFlocData.getString("EventPicture");
            iEventId = jsonFlocData.getInt("EventId");
            iCategoryId = jsonFlocData.getInt("EventCategory");
            iCreaterId = jsonFlocData.getInt("EventCreatorId");
            strEventName = jsonFlocData.getString("EventName");

            String strAddress = jsonFlocData.getString("EventArea")+", "+ jsonFlocData.getString("EventCity")+", "+
                                                                            jsonFlocData.getString("EventState");

            tvStartDate.setText( getResources().getString(R.string.start_date)+" "+formatDate(jsonFlocData.getString("EventStartDate")) );
            tvStartTime.setText( getResources().getString(R.string.start_hour)+" "+jsonFlocData.getString("EventStartHour") + ":"+ jsonFlocData.getString("EventStartMin"));
            tvEndDate.setText( getResources().getString(R.string.end_date)+" "+formatDate(jsonFlocData.getString("EventEndDate")) );
            tvEndTime.setText( getResources().getString(R.string.end_hour)+" "+jsonFlocData.getString("EventEndHour") + ":"+ jsonFlocData.getString("EventEndMin"));
            tvAddress.setText("Address: "+strAddress);
            tvDetails.setText("Description: "+jsonFlocData.getString("EventDescription"));
            MakeTextResizable.makeTextViewResizable(tvDetails, 3, "See More", true);

            Glide
                    .with(this)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(imgFlocPic);


            try {
                JSONObject jsonObject = new JSONObject(getSharedPreference.getString(getResources().getString(R.string.shrdActivityId)) );

                iActivityIdRating = jsonObject.getInt("rate");
                iActivityIdLike = jsonObject.getInt("like");
                iActivityIdReview = jsonObject.getInt("review");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // facebook share
        shareButton = (ShareButton) findViewById(R.id.shareButton);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("http://flocworld.co.in/Event/EventDescription/"+iEventId)
                .setContentUrl(Uri.parse(CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture))
                .build();
        shareButton.setShareContent(content);

        // Google+ share
        /*mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
        URL = CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture;*/

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickLikeEvent(View view) {
        if (!isConnectingToInternet(this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {
            new LikeStoreAsyncTask(this, tvAsyncText, iActivityIdLike, iEventId, iUSerId).postData();
        }
    }

    public void OnClickSubmitReview(View view) {
        if (!isConnectingToInternet(this)) {

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
                new ReviewStoreAsyncTask(this, tvAsyncText, iActivityIdReview,
                        iEventId, iUSerId, txtReview.getText().toString()).postData();
            }
            else {
                CommonUtilities.customToast(this, "Review field is empty.");
            }

        }
    }

    public void onClickBookEvent(View view) {
        if (!isConnectingToInternet(this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {

            new BookEventAsyncTask(this, tvAsyncText,
                    iUSerId, iEventId, iCategoryId, strUserName, strUserEmail, 1).postData();
        }
    }

    public void onClickSubmitReview(View view) {
        if (!isConnectingToInternet(this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {

            new RateEventAsyncTask(this, tvAsyncText,
                    iActivityIdRating, iUSerId, iEventId, iCreaterId, Math.round(ratingBar.getRating()) ).postData();
        }
    }

    @Override
    public void onClickLike(JSONArray jsonArrayAllLike) {
        Intent intent = new Intent(this, FlocDescTopicsActivity.class);
        intent.putExtra("flocTopics", jsonArrayAllLike.toString());
        intent.putExtra("what", "likes");
        startActivity(intent);
    }

    @Override
    public void onClickReview(JSONArray jsonArrayAllReviews) {
        Intent intent = new Intent(this, FlocDescTopicsActivity.class);
        intent.putExtra("flocTopics", jsonArrayAllReviews.toString());
        intent.putExtra("what", "reviews");
        startActivity(intent);
    }

    @Override
    public void onClickBooking(JSONArray jsonArrayAllBooking) {
        Intent intent = new Intent(this, FlocDescTopicsActivity.class);
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

        if (!isConnectingToInternet(this)) {

            rlProgressLayout.setVisibility(View.VISIBLE);
            tvAsyncText.setText(getResources().getString(R.string.strNoInternet));

            findViewById(R.id.prgdlgGetAcivity).setVisibility(View.GONE);

            // stop executing code by return
            return;
        }
        else {
            if(strComment.length() > 0) {
                new ChatAsyncTask(this, tvAsyncText,
                        iUSerId, iEventId, strEventName, strComment ).postData();
            }
        }
    }

    private String formatDate(String dateText) {
        long lDateDob = DateHelper.dobConvertToMillis(dateText);
        String day = DateHelper.getDay(lDateDob);
        String month = DateHelper.getMonth(lDateDob);
        String year = DateHelper.getYear(lDateDob);

        return day + "-" + month + "-" + year;
    }

    public void openSharePopup(View view) {
        // initialize a pop up window type

        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.share_floc_window, null);

        if( strFrom.equals( EnumFlocDescFrom.Archive.toString() ) ||
                strFrom.equals( EnumFlocDescFrom.Completed.toString() ) ||
                strFrom.equals( EnumFlocDescFrom.Pause.toString() ) ||
                strFrom.equals( EnumFlocDescFrom.Cancel.toString() ) ) {
            CommonUtilities.customToast(FlocDescriptionActivity.this, "Not allowed to share Floc for some reason.");
        }

        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21) {
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        View anchor = findViewById(R.id.id_openShareDailog);
        popupWindow.showAsDropDown(anchor, 0, -580);
    }

    public void onClickFacebookShare(View view) {
        popupWindow.dismiss();
        shareButton.performClick();
    }

    public void onClickGoogleShare(View view) {
        popupWindow.dismiss();

        Uri contentUri = getUri();
        ContentResolver cr = this.getContentResolver();
        String mime = cr.getType(contentUri);

        PlusShare.Builder share = new PlusShare.Builder(this);
        share.setText("http://flocworld.co.in/Event/EventDescription/"+iEventId);
        share.addStream(contentUri);
        share.setType(mime);
        startActivityForResult(share.getIntent(), PLUS_ONE_REQUEST_CODE);
    }

    public void onClickWhatsappShare(View view) {
        popupWindow.dismiss();
        /*Uri contentUri = getUri();

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("image*//*");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "http://flocworld.co.in/Event/EventDescription/"+iEventId);
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        whatsappIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            CommonUtilities.customToast(this, "Whatsapp have not been installed.");
        }*/

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout into a view and configure it the way you like
        View inflate = mInflater.inflate(R.layout.share_layout_on_whatsapp, null, false);
        ImageView imgView = (ImageView) inflate.findViewById(R.id.id_iv_share_floc_img);
        AppCompatTextView tvTitle = (AppCompatTextView) inflate.findViewById(R.id.id_tv_share_floc_text);

        imgView.setImageDrawable(imgFlocPic.getDrawable());
        tvTitle.setText(strEventName);

        //Pre-measure the view so that height and width don't remain null.
        inflate.measure(View.MeasureSpec.makeMeasureSpec(inflate.getWidth(), View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(inflate.getHeight(), View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        inflate.layout(inflate.getWidth(), inflate.getHeight(), inflate.getMeasuredWidth(), inflate.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(inflate.getMeasuredWidth(),
                inflate.getMeasuredHeight(),
                Bitmap.Config.RGB_565 );

        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        inflate.draw(c);

        File imagePath = new File(getCacheDir(), "images");
        if(!imagePath.exists()) {
            imagePath.mkdir();
        }

        File newFile = new File(imagePath, "image_"+new Date().getTime()+"_"+".png");

        try {
            if(!newFile.exists()) {
                newFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(newFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri contentUri = FileProvider.getUriForFile(FlocDescriptionActivity.this, "com.cleanslatetech.floc.fileprovider", newFile);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://flocworld.co.in/Event/EventDescription/"+iEventId);

        try {
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        } catch (android.content.ActivityNotFoundException ex) {
            CommonUtilities.customToast(this, "Whatsapp have not been installed.");
        }
    }

    private Uri getUri() {
        imgFlocPic.setDrawingCacheEnabled(true);
        Bitmap bmp = imgFlocPic.getDrawingCache();

        File imagePath = new File(getCacheDir(), "images");
        if(!imagePath.exists()) {
            imagePath.mkdir();
        }

        File newFile = new File(imagePath, "image.png");

        try {
            if( newFile.exists() ) {
                newFile.delete();
            }

            newFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(newFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
            imgFlocPic.setDrawingCacheEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(this, "com.cleanslatetech.floc.fileprovider", newFile);
    }

    public void onClickFlocAppShare(View view) {
        popupWindow.dismiss();

        final EditText emailID = new EditText(this);
        emailID.setHint("Friend Email Id");

        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.setScaleY(0.7f);
        progressBar.setScaleX(0.7f);
        progressBar.setVisibility(View.GONE);

        final TextView textview = new TextView(this);
        textview.setText("Hell");
        textview.setPadding(18, 20, 0, 0);
        textview.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        textview.setVisibility(View.GONE);

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(20, 2, 2, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(emailID);
        layout.addView(progressBar);
        layout.addView(textview);

        // adjust InputType of edittext.
        emailID.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        String userName = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserName));
        alertDialog.setTitle("Invite User To Event");

        // set main layout to alert dialog
        alertDialog.setView(layout);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Send",null);
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        final Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        final Button pButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        pButton.setTextColor(Color.BLACK);
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailId = emailID.getText().toString();
                Boolean flag = true;

                if( !Validations.emailValidate(emailId) ) {
                    flag = false;
                    emailID.setError("field not a valid.");
                }

                if(flag) {
                    new EventInvitationAsyncTask(FlocDescriptionActivity.this,
                            emailId, progressBar, nbutton, iUSerId, iEventId, textview).postData();
                }
            }
        });
    }
}
