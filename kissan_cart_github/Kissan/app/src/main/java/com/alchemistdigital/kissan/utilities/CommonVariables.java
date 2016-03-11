package com.alchemistdigital.kissan.utilities;

import android.os.Environment;

/**
 * Created by user on 2/29/2016.
 */
public class CommonVariables {
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    // Google project id
    public static final String SENDER_ID = "765396915530";

    public static final String DISPLAY_MESSAGE_ACTION =
            "info.alchemistdigital.e_carrier.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    public static final String SCAN_FILE_PATH = Environment.getExternalStorageDirectory() + "/obp";
    public static final String SOCIETY_INSERT_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/create_society.php";
    public static final String LOGGING_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/logging.php";
    public static final String OBP_INSERT_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/cerate_obp.php";
    public static final String SOCIETY_PER_OBP_QUERY_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/getSociety.php";
    public static final String ALL_SOCIETY_BY_ADMIN_QUERY_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/getAllSociety.php";
    public static final String ENQUIRY_INSERT_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/create_enquiry.php";
    public static final String ENQUIRY_PER_OBP_QUERY_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/getEnquiry.php";
    public static final String UPDATE_GCMID_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/updateGCMId.php";
    public static final String ORDER_INSERT_SERVER_URL = "http://bd15a3a2.ngrok.io/kissan_cart/create_order.php";

}