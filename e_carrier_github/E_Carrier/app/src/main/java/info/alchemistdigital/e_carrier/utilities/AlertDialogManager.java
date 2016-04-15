package info.alchemistdigital.e_carrier.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import info.alchemistdigital.e_carrier.R;

/**
 * Created by user on 12/30/2015.
 */
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * @param context - application context  */
    public void showAlertDialog(Context context,String title, String message, boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
