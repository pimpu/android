package com.alchemistdigital.kissan.utilities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by user on 2/25/2016.
 */
public class CommonUtilities {

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

    /*get file name from selected file of sdcard*/
    public static String getFileName(Context context,Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Bitmap createBitmapFromFilePath(String filename){
        File image = new File(CommonVariables.SCAN_FILE_PATH, filename);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        return bitmap;
    }

    public static String getStringImage(String filename) {
        Bitmap bmp = createBitmapFromFilePath(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

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
        Intent intent = new Intent(CommonVariables.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(CommonVariables.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    /**
     * calculate the height of expandable listView without expanded
     * @param expListView
     */
    public static void setListViewHeight(ExpandableListView expListView) {
        ListAdapter listAdapter = expListView.getAdapter();
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, expListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = expListView.getLayoutParams();
        params.height = totalHeight
                + (expListView.getDividerHeight() * (listAdapter.getCount() - 1));

        expListView.setLayoutParams(params);
        expListView.requestLayout();
    }

    // calculate the height of expandable listview dynamically
    public static void setListViewHeightAtExpand(ExpandableListView expListView, int group) {

        ExpandableListAdapter listAdapter = expListView
                .getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(expListView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null,
                    expListView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((expListView.isGroupExpanded(i)) && (i == group))
                    || ((!expListView.isGroupExpanded(i)) && (i == group))) {

                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {

                    View listItem = listAdapter.getChildView(i, j, false, null,
                            expListView);

                    listItem.setLayoutParams(new ViewGroup.LayoutParams(
                            desiredWidth, View.MeasureSpec.UNSPECIFIED));
                    // listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                    listItem.measure(View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                            .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = expListView.getLayoutParams();
        int height = totalHeight
                + (expListView.getDividerHeight() * (listAdapter
                .getGroupCount() - 1));

        if (height < 10) {
            height = 100;
        }
        params.height = height;
        expListView.setLayoutParams(params);
        expListView.requestLayout();

    }

    public static void store_Png_InSdcard(Context context, Bitmap bitmap, String filename) {
        File fn;

        try {  // Try to Save #1

            File myDirectory = new File(Environment.getExternalStorageDirectory(), "obp");

            if(!myDirectory.exists()) {
                myDirectory.mkdirs();
            }

            String IMAGE_PATH = CommonVariables.SCAN_FILE_PATH;

            fn = new File(IMAGE_PATH, filename);

            FileOutputStream out = new FileOutputStream(fn);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
            out.flush();
            out.close();

            Toast.makeText(context,
                    "File is Saved in  " + fn, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void generateNotification(Context context, String message) {
        Log.d("Notification",""+isActivityRunning(context));
//        if( !isActivityRunning(context) ) {
            String title = context.getString(R.string.app_name);

            Intent mainIntent=new Intent(context, SplashScreen.class);

            mainIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setAction(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            Notification notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle(title)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.ic_launcher_logo)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis())
                    .getNotification();
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
//        }
    }
}
