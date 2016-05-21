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

    //    static String SERVER_URL = "http://kissancart.com/obp/AndroidWebservices/";
    //    public static String FILE_UPLOAD_URL = "http://kissancart.com/obp/admin/Enquiry/";

    static String SERVER_URL = "http://221d99b7.ngrok.io/kissan_cart/";
    public static String FILE_UPLOAD_URL = SERVER_URL + "AndroidFileUpload/";

    public static final String SOCIETY_INSERT_SERVER_URL = SERVER_URL +"create_society_byAndroid.php";
    public static final String LOGGING_SERVER_URL = SERVER_URL+"logging_byAndroid.php";
    public static final String OBP_INSERT_SERVER_URL = SERVER_URL+"cerate_obp_byAndroid.php";
    public static final String SOCIETY_PER_OBP_QUERY_SERVER_URL = SERVER_URL+"getSociety_byAndroid.php";
    public static final String ALL_SOCIETY_BY_ADMIN_QUERY_SERVER_URL = SERVER_URL+"getAllSociety_byAndroid.php";
    public static final String ENQUIRY_INSERT_SERVER_URL = SERVER_URL+"create_enquiry_byAndroid.php";
    public static final String ENQUIRY_PER_OBP_QUERY_SERVER_URL = SERVER_URL+"getEnquiry_byAndroid.php";
    public static final String ALL_ORDER_QUERY_SERVER_URL = SERVER_URL+"getAllOrder_byAndroid.php";
    public static final String UPDATE_GCMID_SERVER_URL = SERVER_URL+"updateGCMId_byAndroid.php";
    public static final String ORDER_INSERT_SERVER_URL = SERVER_URL+"create_order_byAndroid.php";
    public static final String QUERY_OBP_DETAIL_SERVER_URL = SERVER_URL+"getObpDetailsAtAdmin.php";
    public static final String UPDATE_SOCIETY_DETAIL_SERVER_URL = SERVER_URL+"updateSocietyDetail.php";
    public static final String DELETE_SOCIETY_DETAIL_SERVER_URL = SERVER_URL+"deleteSocietyDetail.php";
    public static final String OFFLINE_ENQUIRY_INSERT_SERVER_URL = SERVER_URL+"offlineEnquiryInsert.php";
    public static final String OFFLINE_SOCIETY_INSERT_SERVER_URL = SERVER_URL+"offlineSocietyInsert.php";
    public static final String OFFLINE_OBP_INSERT_SERVER_URL = SERVER_URL+"offlineOBPInsert.php";
    public static final String OFFLINE_VENDOR_INSERT_SERVER_URL = SERVER_URL+"offlineVendorInsert.php";
    public static final String DELETE_OBP_SERVER_URL = SERVER_URL+"deleteOBP.php";
    public static final String UPDATE_OBP_SERVER_URL = SERVER_URL+"updateOBP.php";
    public static final String OFFLINE_ORDER_INSERT_SERVER_URL = SERVER_URL+"offlineOrderInsert.php";
    public static final String ALL_OBP_QUERY_SERVER_URL = SERVER_URL+"getAllOBP_byAndroid.php";
    public static final String GET_GROUP_SERVER_URL = SERVER_URL+"getGroupDetailsData.php";
    public static final String GET_CATEGORY_SERVER_URL = SERVER_URL+"getCategoryDetailsData.php";
    public static final String GET_SUBCATEGORY_SERVER_URL = SERVER_URL+"getSubcategoryDetailsData.php";
    public static final String GET_PRODUCTDETAILS_SERVER_URL = SERVER_URL+"getProductDetailsData.php";
    public static final String VENDOR_INSERT_SERVER_URL = SERVER_URL+"cerate_vendor_byAndroid.php";
    public static final String DELETE_VENDOR_SERVER_URL = SERVER_URL+"deleteVendor.php";
    public static final String UPDATE_VENDOR_DETAIL_SERVER_URL = SERVER_URL+"updateVendorDetails.php";

}