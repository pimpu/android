package info.alchemistdigital.e_carrier.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.asynctask.UpdateLatsLongsAsyncTask;

/**
 * Created by user on 1/5/2016.
 */
public class GetAddressFromLatsLong {

    public static void getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        SharedPreferences sharedPreferenceLogin= context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), 0);
        SharedPreferences.Editor editor = sharedPreferenceLogin.edit();

        int loginID = sharedPreferenceLogin.getInt(context.getResources().getString(R.string.loginId), 0);
        String userType = sharedPreferenceLogin.getString(context.getResources().getString(R.string.userType), "");
        String oldPosatlcode = sharedPreferenceLogin.getString(context.getResources().getString(R.string.postalCode), "");

        List<Address> addresses  = null;
        String strPostalCode=null;
        StringBuilder strReturnedAddress = new StringBuilder("");

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(context,""+e.printStackTrace(),Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (addresses != null) {
//            System.out.println(addresses);
            Address returnedAddress = addresses.get(0);

            if( returnedAddress.getPostalCode() != null){
                strPostalCode = returnedAddress.getPostalCode();
                System.out.println("Postal code: "+strPostalCode);
                editor.putString(context.getResources().getString(R.string.postalCode),strPostalCode );
            }
            else {
                int addresslineSize = returnedAddress.getMaxAddressLineIndex();
                Pattern p = Pattern.compile("[0-9]+$");
                Matcher m = p.matcher(returnedAddress.getAddressLine(addresslineSize-1));
                if(m.find()) {
                    strPostalCode = m.group();
                }

                System.out.println("extracted postalcode "+strPostalCode);
                editor.putString(context.getResources().getString(R.string.postalCode), strPostalCode );
            }


            // store postal code on server when user is driver and old and new postal code is different.
            if( userType.equals("driver")  ){
//            if( userType.equals("driver") && !oldPosatlcode.equals(strPostalCode) ){

                ConnectionDetector cd=new ConnectionDetector(context);

                if (cd.isConnectingToInternet()) {
                    new UpdateLatsLongsAsyncTask(context, latitude, longitude, loginID, strPostalCode).execute();
                }
            }

            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
            }

            // it store the Register true value of user for purpose of user is registered with this app.
            editor.putString("fromAddress", strReturnedAddress.toString() );
            editor.putString(context.getResources().getString(R.string.lats),""+latitude);
            editor.putString(context.getResources().getString(R.string.longs),""+longitude);
            editor.commit();

        } else {
            Toast.makeText(context, "My Current loction addressNo Address returned!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getGeoCodeFromLocationName(Context context, String str){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(str, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list != null) {
            Address returnedAddress = list.get(0);
            getAddress(context,returnedAddress.getLatitude(),returnedAddress.getLongitude());
        }

    }

}
