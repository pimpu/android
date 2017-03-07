package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.FlocDescTopicsActivity;
import com.cleanslatetech.floc.activities.FlocDescriptionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pimpu on 3/5/2017.
 */

public class PopulateFloDescData {
    private Context context;
    private JSONObject json;
    private int iUSerId, From;

    public PopulateFloDescData(Context context, JSONObject json, int iUSerId, int From) {
        this.context = context;
        this.json = json;
        this.iUSerId = iUSerId;
        this.From = From;
    }

    public void populatesData() {
        try {
            if( !json.has(CommonVariables.TAG_ERROR) ) {
                json.put(CommonVariables.TAG_ERROR, false);
            }

            if( !json.getBoolean("Error") ) {

                AppCompatActivity appCompatContext = (AppCompatActivity) context;

                switch (From) {
                    case CommonVariables.ACTIVITY:
                        showAllData(appCompatContext);
                        break;

                    case CommonVariables.BOOKING:
                        showBookingData(appCompatContext);
                        break;

                    case CommonVariables.CHAT:
                        showChatData(appCompatContext);
                        break;

                    case CommonVariables.LIKE:
                        showLikeData(appCompatContext);
                        break;

                    case CommonVariables.RATE:
                        showRateData(appCompatContext);
                        break;

                    case CommonVariables.REVIEW:
                        showReviewData(appCompatContext);
                        break;
                }

            } else {
                // show error toast
                CommonUtilities.customToast(context, json.getString(CommonVariables.TAG_MESSAGE_OBJ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showReviewData(AppCompatActivity appCompatContext) {
        AppCompatTextView tvReviewCount = (AppCompatTextView) appCompatContext.findViewById(R.id.id_reviews_count);

        try {
            JSONArray jsonArrayReview = new JSONArray();

            if( !json.isNull("Review") ) {
                jsonArrayReview = json.getJSONArray("Review");
            }

            if (json.has("Review")) {
                tvReviewCount.setText(""+jsonArrayReview.length());
            }

            final JSONArray finalJsonArrayReview = jsonArrayReview;

            tvReviewCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FlocDescriptionActivity)context).interfaceFlocDescTopics.onClickReview(finalJsonArrayReview);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showRateData(AppCompatActivity appCompatContext) {
        AppCompatRatingBar tvRatingBar = (AppCompatRatingBar) appCompatContext.findViewById(R.id.ratingBar);
        // set rating to rating bar
        try {
            tvRatingBar.setRating((float) json.getInt("Rate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showLikeData(AppCompatActivity appCompatContext) {
        AppCompatTextView tvLikes = (AppCompatTextView) appCompatContext.findViewById(R.id.id_likes_count);
        AppCompatImageButton imgbtnUnlikeTint = (AppCompatImageButton) appCompatContext.findViewById(R.id.img_btn_unlike_event);
        AppCompatImageButton imgbtnLikeTint = (AppCompatImageButton) appCompatContext.findViewById(R.id.img_btn_like_event);

        try {
            JSONArray jsonArrayLike = new JSONArray();

            if( !json.isNull("Like") ) {
                jsonArrayLike = json.getJSONArray("Like");
            }

            if(json.has("EventLike")) {
                tvLikes.setText(""+json.getInt("EventLike"));
            } else if(json.has("Count")) {
                tvLikes.setText(""+json.getInt("Count"));
            }

            // search user id in like array
            // and set action on like layout
            if (jsonArrayLike.length() > 0) {

                for(int k = 0, numLikes = jsonArrayLike.length(); k < numLikes; k++) {
                    if( jsonArrayLike.getJSONObject(k).getInt("UserId") == iUSerId) {
                        imgbtnLikeTint.setVisibility(View.VISIBLE);
                        imgbtnUnlikeTint.setVisibility(View.GONE);
                        break;
                    }
                    else {
                        imgbtnLikeTint.setVisibility(View.GONE);
                        imgbtnUnlikeTint.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                imgbtnLikeTint.setVisibility(View.GONE);
                imgbtnUnlikeTint.setVisibility(View.VISIBLE);
            }

            final JSONArray finalJsonArrayLike = jsonArrayLike;
            tvLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FlocDescriptionActivity)context).interfaceFlocDescTopics.onClickLike(finalJsonArrayLike);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showChatData(AppCompatActivity appCompatContext) {
        JSONArray jsonArrayChat = new JSONArray();

        if( !json.isNull("Chat") ) {
            try {
                jsonArrayChat = json.getJSONArray("Chat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // sent chats data to FlocDescription activity
        ((FlocDescriptionActivity)context).interfaceFlocDescTopics.getChatData(jsonArrayChat);
    }

    private void showBookingData(AppCompatActivity appCompatContext) {
        AppCompatTextView tvBookingCount = (AppCompatTextView) appCompatContext.findViewById(R.id.id_booking_count);
        AppCompatTextView tvBookedTest = (AppCompatTextView) appCompatContext.findViewById(R.id.id_tv_booked);
        AppCompatButton btnBookedEvent = (AppCompatButton) appCompatContext.findViewById(R.id.id_btn_booked_event);

        try {

            JSONArray jsonArrayBooking = new JSONArray();

            if( !json.isNull("Booking") ) {
                jsonArrayBooking = json.getJSONArray("Booking");
            }

            if (json.has("BookCount")) {
                tvBookingCount.setText(""+json.getInt("BookCount"));
            }
            else {
                tvBookingCount.setText(""+jsonArrayBooking.length());
            }

            if( FlocDescriptionActivity.strFrom.equals("RecentFloc") ) {
                tvBookedTest.setVisibility(View.GONE);
                btnBookedEvent.setVisibility(View.GONE);
            }
            else {
                if (jsonArrayBooking.length() > 0) {

                    for(int booked = 0, numBooks = jsonArrayBooking.length(); booked < numBooks; booked++) {
                        if( jsonArrayBooking.getJSONObject(booked).getInt("UserId") == iUSerId) {

                            tvBookedTest.setVisibility(View.VISIBLE);
                            btnBookedEvent.setVisibility(View.GONE);
                            break;
                        }
                        else {
                            tvBookedTest.setVisibility(View.GONE);
                            btnBookedEvent.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    tvBookedTest.setVisibility(View.GONE);
                    btnBookedEvent.setVisibility(View.VISIBLE);
                }
            }

            final JSONArray finalJsonArrayBooking = jsonArrayBooking;
            tvBookingCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FlocDescriptionActivity)context).interfaceFlocDescTopics.onClickBooking(finalJsonArrayBooking);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showAllData(AppCompatActivity appCompatContext) {
        if( json.has("OrganizerName") ) {
            AppCompatTextView tvOrganiser = (AppCompatTextView) appCompatContext.findViewById(R.id.id_organiser_name);

            try {
                tvOrganiser.setText(json.getString("OrganizerName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        showBookingData(appCompatContext);
        showChatData(appCompatContext);
        showLikeData(appCompatContext);
        showRateData(appCompatContext);
        showReviewData(appCompatContext);
    }
}
