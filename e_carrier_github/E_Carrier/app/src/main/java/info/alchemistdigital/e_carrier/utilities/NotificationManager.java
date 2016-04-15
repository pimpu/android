package info.alchemistdigital.e_carrier.utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.SplashScreen;

import static info.alchemistdigital.e_carrier.utilities.CommonUtilities.isActivityRunning;

/**
 * Created by user on 1/19/2016.
 */
public class NotificationManager {
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    public static void generateNotification(Context context, String message) {
        if( !isActivityRunning(context) ){
            String title = context.getString(R.string.app_name).toString();

            Intent mainIntent=new Intent(context, SplashScreen.class);

            mainIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setAction(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            Notification notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle(title)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.logo)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis())
                    .getNotification();
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }


    }
}
