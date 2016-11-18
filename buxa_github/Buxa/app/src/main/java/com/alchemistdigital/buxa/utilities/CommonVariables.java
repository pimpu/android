package com.alchemistdigital.buxa.utilities;

/**
 * Created by user on 8/12/2016.
 */
public class CommonVariables {
    public static final String TAG_ERROR = "error";
    public static final String TAG_MESSAGE = "message";
    public static final String DISPLAY_MESSAGE_ACTION = "com.alchemistdigital.buxa.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";
    //    -------------- place api -------------------
    public static final String LOG_TAG = "Google Places Autocomplete";
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";

    public static final String API_KEY = "AIzaSyCyjSSwYtYv4r84kESFyVz2m-edkKc0N54";

    //   -----------------------  Firebase cloud messaging  ----------------------

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    static String SERVER_URL = "http://54df6ffa.ngrok.io/buxa/v1/";
//    static String SERVER_URL = "http://alchemistdigital.net/android_webservices/v1/";

    public static final String COMPANY_REGISTER_SERVER_URL = SERVER_URL + "register";
    public static final String COMPANY_LOGIN_SERVER_URL = SERVER_URL + "login";
    public static final String QUERY_COMMODITY_SERVER_URL = SERVER_URL + "commodities";
    public static final String QUERY_CUSTOM_LOACTION_SERVER_URL = SERVER_URL + "customlocation";
    public static final String QUERY_CUSTOM_CLEARANCE_CATEGORY_SERVER_URL = SERVER_URL + "customclearancecategory";
    public static final String QUERY_SHIPMENT_TYPE_SERVER_URL = SERVER_URL + "typeofshipment";
    public static final String QUERY_TRANSPORT_TYPE_SERVER_URL = SERVER_URL + "transporttype";
    public static final String QUERY_TRANSPORT_SERVICE_SERVER_URL = SERVER_URL + "transportservice";
    public static final String QUERY_PACKAGING_TYPE_SERVER_URL = SERVER_URL + "packagingtype";
    public static final String INSERT_TRANSPOERTATION_SERVER_URL = SERVER_URL + "inserttransport";
    public static final String INSERT_CUSTOM_CLEARANCE_SERVER_URL = SERVER_URL + "insertcustomclearance";
    public static final String INSERT_FREIGHT_FORWARDING_SERVER_URL = SERVER_URL + "insertfreightforwarding";
    public static final String UPDATE_GCM_REGISTERED_ID_SERVER_URL = SERVER_URL + "updategcmid";
    public static final String QUERY_CFS_ADDRESS_SERVER_URL = SERVER_URL + "getcfsadresses";
    public static final String SEND_EMAIL_FLAG_SERVER_URL = SERVER_URL + "sendmail";
    public static final String ACCPET_ENQUIRY_SERVER_URL = SERVER_URL + "acceptenquiry";
    public static final String CANCEL_ENQUIRY_SERVER_URL = SERVER_URL + "cancelenquiry";


}
