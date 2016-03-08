package com.alchemistdigital.kissan.utilities;

import android.os.Environment;

/**
 * Created by user on 2/29/2016.
 */
public class CommonVariables {
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static final String SCAN_FILE_PATH = Environment.getExternalStorageDirectory() + "/obp";
    public static final String SOCIETY_INSERT_SERVER_URL = "http://44a86c17.ngrok.io/kissan_cart/create_society.php";
    public static final String LOGGING_SERVER_URL = "http://44a86c17.ngrok.io/kissan_cart/logging.php";
    public static final String OBP_INSERT_SERVER_URL = "http://44a86c17.ngrok.io/kissan_cart/cerate_obp.php";
    public static final String SOCIETY_PER_OBP_INSERT_SERVER_URL = "http://44a86c17.ngrok.io/kissan_cart/getSocietyPerOBP.php";
    public static final String ENQUIRY_INSERT_SERVER_URL = "http://44a86c17.ngrok.io/kissan_cart/create_enquiry.php";
}