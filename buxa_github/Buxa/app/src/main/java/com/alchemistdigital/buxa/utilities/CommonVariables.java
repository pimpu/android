package com.alchemistdigital.buxa.utilities;

/**
 * Created by user on 8/12/2016.
 */
public class CommonVariables {
    public static final String TAG_ERROR = "error";
    public static final String TAG_MESSAGE = "message";
    public static final String DISPLAY_MESSAGE_ACTION = "com.alchemistdigital.buxa.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";

    static String SERVER_URL = "http://63509dd6.ngrok.io/buxa/v1/";
//    static String SERVER_URL = "http://alchemistdigital.net/buxa/v1/";

    public static final String COMPANY_REGISTER_SERVER_URL = SERVER_URL + "register";
    public static final String COMPANY_LOGIN_SERVER_URL = SERVER_URL + "login";
    public static final String QUERY_COMMODITY_SERVER_URL = SERVER_URL + "commodities";
    public static final String QUERY_CUSTOM_LOACTION_SERVER_URL = SERVER_URL + "customlocation";
    public static final String QUERY_CUSTOM_CLEARANCE_CATEGORY_SERVER_URL = SERVER_URL + "customclearancecategory";
    public static final String QUERY_SHIPMENT_TYPE_SERVER_URL = SERVER_URL + "typeofshipment";
    public static final String QUERY_TRANSPORT_TYPE_SERVER_URL = SERVER_URL + "transporttype";
    public static final String QUERY_TRANSPORT_SERVICE_SERVER_URL = SERVER_URL + "transportservice";
}
