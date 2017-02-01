package com.cleanslatetech.floc.utilities;

/**
 * Created by pimpu on 1/13/2017.
 */

public class CommonVariables {
    public static final String TAG_ERROR = "Error";
    public static final String TAG_MESSAGE = "Message";
    public static final String TAG_MESSAGE_OBJ = "Msg";
    public static final String TAG_ID = "Id";
    public static final String DISPLAY_MESSAGE_ACTION = "com.cleanslatetech.floc.DISPLAY_MESSAGE";

    // server api calling strings
    public static final String USER_REGISTER_SERVER_URL = "http://demo.floc.world/api/AccountApi/Register";
    public static final String USER_LOGIN_SERVER_URL = "http://demo.floc.world/api/AccountApi/Login";

    private static String domian_server_url = "http://demo.floc.world/api/MobileOperationApi/";
    public static final String INTEREST_CATEGORY_SERVER_URL = "http://demo.floc.world/img/category/";
    public static final String EVENT_IMAGE_SERVER_URL = "http://demo.floc.world/img/";

    public static final String OTP_CONFIRM_SERVER_URL = domian_server_url + "SendOtp";
    public static final String GET_INTEREST_CATEGORY_SERVER_URL = domian_server_url + "GetCategory";
    public static final String SET_USER_INTEREST_SERVER_URL = domian_server_url + "UserInterest";
    public static final String GET_ALL_EVENTS_SERVER_URL = domian_server_url + "GetEvent";
}
