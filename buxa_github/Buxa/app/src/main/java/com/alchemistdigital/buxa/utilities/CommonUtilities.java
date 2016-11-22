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
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;

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
            System.err.println("ExceptionGetEmails: " + e);
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

    /***
     * Checks that application runs first time and write flag at SharedPreferences
     * @return true if 1st time
     */
    public static boolean isFirstTime(Context context) {
        GetSharedPreference getSharedPreference = new GetSharedPreference(context);
        SetSharedPreference setSharedPreference = new SetSharedPreference(context);

        boolean ranBefore = getSharedPreference.getIsFirstTime(context.getResources().getString(R.string.runBefore));
        if (!ranBefore) {
            // first time
            setSharedPreference.setIsFirstTime(context.getResources().getString(R.string.runBefore), true);
        }
        return !ranBefore;
    }
}
