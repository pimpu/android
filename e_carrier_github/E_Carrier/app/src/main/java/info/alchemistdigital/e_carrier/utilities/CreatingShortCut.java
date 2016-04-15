package info.alchemistdigital.e_carrier.utilities;

import android.content.Context;
import android.content.Intent;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.SplashScreen;

/**
 * Created by user on 12/30/2015.
 */
public class CreatingShortCut {
    public static void addShortcut(Context context) {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(context,
                SplashScreen.class);

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
}
