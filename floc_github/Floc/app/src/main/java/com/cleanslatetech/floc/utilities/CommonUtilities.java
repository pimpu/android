package com.cleanslatetech.floc.utilities;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.BaseAppCompactActivity;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.activities.LoginActivity;
import com.cleanslatetech.floc.activities.SelectInterestActivity;
import com.cleanslatetech.floc.activities.SignupOptionActivity;
import com.cleanslatetech.floc.asynctask.SocialLoginAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
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
import java.net.URISyntaxException;
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
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String id = acct.getId();
//            System.out.println("Name: " + personName + ", email: " + email+", id: "+id);

            new SocialLoginAsyncTask( context, email, "Google", id, personPhotoUrl).postData();

        } else {
            // intet for next activity
            handleIntentWhenSignOut(context);
        }
    }

    public static void handleIntentWhenSignIn(Context context, String type, String name, String email, int id) {
        System.out.println("handleIntentWhenSignIn-Login Type: "+type);

        SetSharedPreference setSharedPreference = new SetSharedPreference(context);

        setSharedPreference.setBoolean(context.getResources().getString(R.string.isAppSignIn),true);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdLoginType), type);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdUserName), name);
        setSharedPreference.setInt(context.getResources().getString(R.string.shrdLoginId), id);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdUserEmail), email);

        if (!isConnectingToInternet(context)) {
            CommonUtilities.customToast(context, context.getResources().getString(R.string.strNoInternet));

            // stop executing code by return
            return;
        }
        else {
            boolean isAvailInterest = new GetSharedPreference(context).getBoolean(context.getResources().getString(R.string.shrdIsInterestSelected));
            if ( isAvailInterest ) {
                context.startActivity(new Intent(context, HomeActivity.class));
            } else  {
                context.startActivity(new Intent(context, SelectInterestActivity.class));
            }
            ((AppCompatActivity)context).finish();
        }
    }

    public static void handleIntentWhenSignOut(Context context) {
        ((AppCompatActivity)context).overridePendingTransition(0,0);
        context.startActivity(
                new Intent(context, SignupOptionActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) );

        SetSharedPreference setSharedPreference = new SetSharedPreference(context);

        setSharedPreference.setBoolean(context.getResources().getString(R.string.isAppSignIn), false);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdLoginType), null);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdUserName), null);
        setSharedPreference.setInt(context.getResources().getString(R.string.shrdLoginId), 0);
        setSharedPreference.setString(context.getResources().getString(R.string.shrdUserEmail), null);

        setSharedPreference.setString(context.getResources().getString(R.string.shrdAllCategoryList), null);
        setSharedPreference.setBoolean(context.getResources().getString(R.string.shrdIsInterestSelected), false);
        setSharedPreference.setStringSet(context.getResources().getString(R.string.shrdSelectedCategory), null);
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

    public static void customToast(Context context, String msg) {
        LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) ((AppCompatActivity)context).findViewById(R.id.toast_layout_root));

        AppCompatTextView text= (AppCompatTextView) layout.findViewById(R.id.toast_text);
        text.setText(msg);

        Toast toast = new Toast(((AppCompatActivity)context).getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1);
    }


}
