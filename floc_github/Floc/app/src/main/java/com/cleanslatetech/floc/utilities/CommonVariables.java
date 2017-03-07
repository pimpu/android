package com.cleanslatetech.floc.utilities;

import android.Manifest;

/**
 * Created by pimpu on 1/13/2017.
 */

public class CommonVariables {
    public static final String TAG_ERROR = "Error";
    public static final String TAG_MESSAGE = "Message";
    public static final String TAG_MESSAGE_OBJ = "Msg";
    public static final String TAG_ID = "Id";
    public static final String DISPLAY_MESSAGE_ACTION = "com.cleanslatetech.floc.DISPLAY_MESSAGE";

    public static final String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int REQUEST_PERMISSION = 101;

    public static final int ACTIVITY = 1;
    public static final int BOOKING = 2;
    public static final int CHAT = 3;
    public static final int LIKE = 4;
    public static final int RATE = 5;
    public static final int REVIEW = 6;



    // server api calling strings
    public static final String USER_REGISTER_SERVER_URL = "http://demo.floc.world/api/AccountApi/Register";
    public static final String USER_LOGIN_SERVER_URL = "http://demo.floc.world/api/AccountApi/Login";

    public static final String INTEREST_CATEGORY_SERVER_URL = "http://demo.floc.world/img/category/";
    public static final String EVENT_IMAGE_SERVER_URL = "http://demo.floc.world/img/";

    private static String domian_server_url = "http://demo.floc.world/api/MobileOperationApi/";
    public static final String OTP_CONFIRM_SERVER_URL = domian_server_url + "SendOtp";
    public static final String GET_INTEREST_CATEGORY_SERVER_URL = domian_server_url + "GetCategory";
    public static final String SET_USER_INTEREST_SERVER_URL = domian_server_url + "UserInterest";
    public static final String GET_ALL_EVENTS_SERVER_URL = domian_server_url + "GetEvent";
    public static final String GET_ALL_RECENT_SERVER_URL = domian_server_url + "GetRecent";
    public static final String CREATE_FLOC_SERVER_URL = domian_server_url + "CreateFloc";
    public static final String POST_IMAGE_SERVER_URL = domian_server_url + "Post";

    private static String domian_event_server_url = "http://demo.floc.world/api/ActivityApi/";
    public static final String GET_EVENT_DETAILS_SERVER_URL = domian_event_server_url + "GetActivites";
    public static final String EVENT_LIKE_SERVER_URL = domian_event_server_url + "LikeEvent";
    public static final String EVENT_REVIEWSERVER_URL = domian_event_server_url + "WriteReview";
    public static final String EVENT_BOOKING_SERVER_URL = domian_event_server_url + "EventBooking";
    public static final String EVENT_RATING_SERVER_URL = domian_event_server_url + "Rating";
    public static final String EVENT_INVITATION_SERVER_URL = domian_event_server_url + "EventInvitation";
    public static final String GROUP_CHAT_SERVER_URL = domian_event_server_url + "GroupChat";
    public static final String FLOC_LIST_SERVER_URL = domian_event_server_url + "GetFlocList";




}
