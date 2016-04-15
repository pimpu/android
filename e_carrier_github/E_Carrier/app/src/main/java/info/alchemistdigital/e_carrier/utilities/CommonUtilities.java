package info.alchemistdigital.e_carrier.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.alchemistdigital.e_carrier.model.Email_Account_Item;

/**
 * Created by user on 12/30/2015.
 */
public class CommonUtilities {
    // give your server registration url here
    static String serverLink = "http://7d86aa2e.ngrok.io/E_Carrier/";
    public static final String USER_LOGIN_SERVER_URL                = serverLink + "create_userLogin.php";
    public static final String UPDATE_GCMID_SERVER_URL              = serverLink + "update_gcmid.php";
    public static final String LOGIN_SERVER_URL                     = serverLink + "loggedIn.php";
    public static final String LOGOT_SERVER_URL                     = serverLink + "logout.php";
    public static final String UPLOAD_ENQUIRY_SERVER_URL            = serverLink + "customerRequest.php";
    public static final String DRIVER_ENQUIRY_RESPONCE_SERVER_URL   = serverLink + "driverEnquiryResponse.php";
    public static final String UPDATE_LATS_LONGS_SERVER_URL         = serverLink + "update_driver_lats_longs.php";
    public static final String QUERY_ENQUIRY_SERVER_URL             = serverLink + "GetEnquiryDetails.php";
    public static final String SERVICE_COMPLETE_SERVER_URL          = serverLink + "service_complete.php";
    public static final String DRIVER_TRACKING_SERVER_URL           = serverLink + "driverTrackByEnquiry.php";
    public static final String FORGOT_PASSWORD_SERVER_URL           = serverLink + "getPassword.php";

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    // Google project id
    public static final String SENDER_ID = "172342830718";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "E-Carrier GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "info.alchemistdigital.e_carrier.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static ArrayList<String> getVehicleType(String  weight, String unit, String type){
        String[] vehicles = {"MARUTI VAN (Cargo)","TATA ZIP","TATA ACE","TATA 407","TATA 709","TATA 909","TATA 1109 / EICHER 1110"};
        ArrayList<String> spinnerArray = new ArrayList<String>();

        Float floatweight = Float.valueOf(weight);
        if(unit.equals("Tonne"))
        {
            floatweight = floatweight * 1000;
        }

        if( floatweight <= (float)600  && (type == "Closed" || type == "Open" ) ){
            spinnerArray.add( vehicles[0] );
        }

        if( floatweight <= (float)600 && (type == "Closed" || type == "Open" ) ){
            spinnerArray.add( vehicles[1] );
        }

        if( ( floatweight > (float)600 && floatweight <= (float)800 ) && (type == "Closed" || type == "Open" )  ){
            spinnerArray.add( vehicles[2] );
        }

        if( (floatweight > (float) 800 && floatweight <= (float)2500) && (type == "Closed" || type == "Open" )  ) {
            spinnerArray.add( vehicles[3] );
        }

        if ( (floatweight > (float)2500 && floatweight <= (float)5000) && (type == "Closed" || type == "Open" )  ) {
            spinnerArray.add( vehicles[4] );
        }

        if ( (floatweight > (float)5000 && floatweight <= (float)6000) && (type == "Closed" || type == "Open" )  ) {
            spinnerArray.add( vehicles[5] );
        }

        if ( (floatweight > (float) 6000 && floatweight <= (float)7500) && (type == "Closed" || type == "Open" )  ) {
            spinnerArray.add( vehicles[6] );
        }

        return spinnerArray;
    }

    public static ArrayList<String> getWeight(String unit) {
        ArrayList<String> spinnerArray = new ArrayList<String>();
        if(unit.equals("Kg")){
            spinnerArray.add("600");
            spinnerArray.add("800");
            spinnerArray.add("2500");
            spinnerArray.add("5000");
            spinnerArray.add("6000");
            spinnerArray.add("7500");
        }
        else {
            spinnerArray.add("0.6");
            spinnerArray.add("0.8");
            spinnerArray.add("2.5");
            spinnerArray.add("5");
            spinnerArray.add("6");
            spinnerArray.add("7.5");
        }
        return spinnerArray;
    }

    public static Map<String, String> getDimension(String vehicleName) {
        String[] vehicles = {"MARUTI VAN (Cargo)","TATA ZIP","TATA ACE","TATA 407","TATA 709","TATA 909","TATA 1109 / EICHER 1110"};
        Map<String,String> dimensionArray = new HashMap<String,String>();
        int index = Arrays.asList(vehicles).indexOf(vehicleName);

        switch (index){
            case 0 :
                dimensionArray.put("height","4");
                dimensionArray.put("length", "4");
                dimensionArray.put("width","4");
                break;

            case 1 :
                dimensionArray.put("height","5.5");
                dimensionArray.put("length","4.5");
                dimensionArray.put("width","4.5");
                break;

            case 2 :
                dimensionArray.put("height","6.5");
                dimensionArray.put("length","6");
                dimensionArray.put("width","5");
                break;

            case 3 :
                dimensionArray.put("height","7");
                dimensionArray.put("length","10.5");
                dimensionArray.put("width","7");
                break;

            case 4 :
                dimensionArray.put("height","7.5");
                dimensionArray.put("length","14");
                dimensionArray.put("width","7");
                break;

            case 5 :
                dimensionArray.put("height","7.5");
                dimensionArray.put("length","17");
                dimensionArray.put("width","7");
                break;

            case 6 :
                dimensionArray.put("height","8.5");
                dimensionArray.put("length","19");
                dimensionArray.put("width","8");
                break;
        }

        return dimensionArray;
    }

    public static Map<String, String> getManPowerPriceByVehicle(String vehicleType) {
        String[] vehicles = {"MARUTI VAN (Cargo)","TATA ZIP","TATA ACE","TATA 407","TATA 709","TATA 909","TATA 1109 / EICHER 1110"};
        int index = Arrays.asList(vehicles).indexOf(vehicleType);
        Map<String,String> PriceArray = new HashMap<String,String>();

        switch (index){
            case 0 :
            case 1 :
                PriceArray.put("manpower","50");
                PriceArray.put("delivery","400");
                PriceArray.put("labour","100");
                break;

            case 2 :
                PriceArray.put("manpower","75");
                PriceArray.put("delivery","500");
                PriceArray.put("labour","150");
                break;

            case 3 :
                PriceArray.put("manpower","80");
                PriceArray.put("delivery","800");
                PriceArray.put("labour","150");
                break;

            case 4 :
                PriceArray.put("manpower","80");
                PriceArray.put("delivery","1500");
                PriceArray.put("labour","200");
                break;

            case 5 :
                PriceArray.put("manpower","100");
                PriceArray.put("delivery","2000");
                PriceArray.put("labour","250");
                break;

            case 6 :
                PriceArray.put("manpower","150");
                PriceArray.put("delivery","2500");
                PriceArray.put("labour","400");
                break;
        }

        return PriceArray;
    }

    public static String apostropheEscapeChar(String name){
        return name.replace("'","''");
    }

    public static ArrayList<String> convertToArrayList(String data){
        ArrayList<String> arrayList = new ArrayList<String>();

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(data);
        while (m.find()) {
            arrayList.add(m.group(1));
        }
        return arrayList;
    }

    public static  double distanceCalculatorFromLatLong(double lat1, double long1, double lat2, double long2){
        double _eQuatorialEarthRadius = 6371;
        double _d2r = (Math.PI / 180D);

        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

    public static HashMap<String,String> convertToKeyValuePair(String data){
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(data);
        ArrayList al = new ArrayList();
        HashMap keyValue = new HashMap();

        while (m.find()) {
            al.add(m.group(1));
        }

        for(int i = 1 ; i < al.size()+1 ; i+=2 ){
            keyValue.put(al.get(i - 1), al.get(i));
        }

        return keyValue;
    }

    public static boolean isActivityRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    public static ArrayList<Email_Account_Item> getEmailsData(FragmentActivity activity) {
        ArrayList<Email_Account_Item> accountsList = new ArrayList<Email_Account_Item>();

        //Getting all registered Google Accounts;
        /*try {
            Account[] accounts = AccountManager.get(activity).getAccountsByType("com.google");
            for (Account account : accounts) {
                Email_Account_Item item = new Email_Account_Item(account.type, account.name);
                accountsList.add(item);
            }
        } catch (Exception e) {
            Log.i("ExceptionGetEmails", "Exception:" + e);
        }*/

        //For all registered accounts;
		try {
			Account[] accounts = AccountManager.get(activity).getAccounts();
			for (Account account : accounts) {
                if(account.name.contains("@")){
                    Email_Account_Item item = new Email_Account_Item( account.type, account.name);
                    accountsList.add(item);
                }
			}
		} catch (Exception e) {
			Log.i("ExceptionGetEmails", "Exception:" + e);
		}
        for( int i = 0 ; i < accountsList.size() ; i++ ){
            String name = accountsList.get(i).getName();
            for( int j=i+1 ; j < accountsList.size() ; j++ ){
                if( name.equals( accountsList.get(j).getName() )){
                    accountsList.remove(j);
                }
            }

        }

        return accountsList;
    }
}
