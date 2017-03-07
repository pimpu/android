package com.cleanslatetech.floc.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.activities.LoginActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.loopj.android.http.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdUserEmail), email);


        if (!isConnectingToInternet(context)) {
            CommonUtilities.customToast(context, context.getResources().getString(R.string.strNoInternet));

            // stop executing code by return
            return;
        }
        else {
            context.startActivity(new Intent(context, HomeActivity.class));
            ((AppCompatActivity)context).finish();
        }
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

    public static String getStringImage(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static void customToast(Context context, String msg) {
        LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) ((AppCompatActivity)context).findViewById(R.id.toast_layout_root));

        AppCompatTextView text= (AppCompatTextView) layout.findViewById(R.id.toast_text);
        text.setText(msg);

        Toast toast = new Toast(((AppCompatActivity)context).getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
