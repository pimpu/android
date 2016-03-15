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

    static String SERVER_URL = "http://2e045bf7.ngrok.io/kissan_cart/";

    public static String FILE_UPLOAD_URL = SERVER_URL+"AndroidFileUpload/uploads/";
//    public static String FILE_UPLOAD_URL = "http://kissancart.com/obp/admin/Enquiry/";

    public static final String SOCIETY_INSERT_SERVER_URL = SERVER_URL +"create_society_byAndroid.php";
    public static final String LOGGING_SERVER_URL = SERVER_URL+"logging_byAndroid.php";
    public static final String OBP_INSERT_SERVER_URL = SERVER_URL+"cerate_obp_byAndroid.php";
    public static final String SOCIETY_PER_OBP_QUERY_SERVER_URL = SERVER_URL+"getSociety_byAndroid.php";
    public static final String ALL_SOCIETY_BY_ADMIN_QUERY_SERVER_URL = SERVER_URL+"getAllSociety_byAndroid.php";
    public static final String ENQUIRY_INSERT_SERVER_URL = SERVER_URL+"create_enquiry_byAndroid.php";
    public static final String ENQUIRY_PER_OBP_QUERY_SERVER_URL = SERVER_URL+"getEnquiry_byAndroid.php";
    public static final String UPDATE_GCMID_SERVER_URL = SERVER_URL+"updateGCMId_byAndroid.php";
    public static final String ORDER_INSERT_SERVER_URL = SERVER_URL+"create_order_byAndroid.php";
    public static final String QUERY_OBP_DETAIL_SERVER_URL = SERVER_URL+"getObpDetailsAtAdmin.php";

}