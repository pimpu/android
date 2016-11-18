package com.alchemistdigital.buxa.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.activities.SplashScreen;
import com.alchemistdigital.buxa.activities.StartupActivity;
import com.alchemistdigital.buxa.model.Email_Account_Item;

import java.util.ArrayList;

/**
 * Created by user on 8/11/2016.
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

    /**
     * create shortcut when user successfully register.
     *
     * @param context
     */
    public static void addShortcut(Context context) {
        //on Home screen
        Intent shortcutIntent = new Intent(context,StartupActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
                        R.mipmap.ic_launcher));
        addIntent.putExtra("duplicate",false);

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
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

    // get register email data from android device
    public static ArrayList<Email_Account_Item> getEmailsData(Context context) {
        ArrayList<Email_Account_Item> accountsList = new ArrayList<Email_Account_Item>();

        //For all registered accounts;
        try {
            Account[] accounts = AccountManager.get(context).getAccounts();
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

    public static void generateNotification(Context context, String message) {
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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .getNotification();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
