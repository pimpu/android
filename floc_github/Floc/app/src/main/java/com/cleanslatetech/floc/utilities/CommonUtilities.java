package com.cleanslatetech.floc.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.activities.LoginActivity;
import com.cleanslatetech.floc.activities.SelectInterestsActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pimpu on 1/2/2017.
 */

public class CommonUtilities {
    private static ProgressDialog mProgressDialog;

    public static Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

            return bundle;
        }
        catch(JSONException e) {
            System.out.println("Error parsing JSON");
        }
        return null;
    }

    // it handle google callbacks results.
    public static void handleGoogleSignInResult(Context context, GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
//            System.out.println("Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl);

            // intet for next activity
            handleIntentWhenSignIn(context,  context.getResources().getString(R.string.googleLogin), true, personName, email, 0);

        } else {

            // intet for next activity
            handleIntentWhenSignOut(context, false);
        }
    }

    public static void handleIntentWhenSignIn(Context context, String type, boolean isSignIn,
                                              String name,String email, int id) {
        System.out.println("handleIntentWhenSignIn-Login Type: "+type);
        System.out.println("handleIntentWhenSignIn-issign: "+isSignIn);


        new SetSharedPreference(context).setBoolean(context.getResources().getString(R.string.isAppSignIn),isSignIn);
        new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdLoginType), type);
        new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdUserName), name);
        new SetSharedPreference(context).setInt(context.getResources().getString(R.string.shrdLoginId), id);

        // not getting email id from user, when user logging.
        // so from logging activity, email="";
        // dont store email id in preference at time of loggin.
        if(email != null) {
            if(email.length() > 0 ) {
                new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdUserEmail), email);
            }
        }

        context.startActivity(new Intent(context, HomeActivity.class));
        ((AppCompatActivity)context).finish();
    }

    public static void handleIntentWhenSignOut(Context context, boolean isSingOut) {
        ((AppCompatActivity)context).overridePendingTransition(0,0);
        context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) );

        new SetSharedPreference(context).setBoolean(context.getResources().getString(R.string.isAppSignIn), isSingOut);
    }

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public static AccessToken isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null)
            return accessToken;
        return null;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public static boolean isConnectingToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}
