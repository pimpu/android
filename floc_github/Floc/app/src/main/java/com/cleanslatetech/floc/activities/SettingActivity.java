package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;

public class SettingActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        super.setToolBar(getResources().getString(R.string.setting));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickChangePwd(View view) {
        final EditText oldPwd = new EditText(this);
        oldPwd.setHint("old Password");
        final EditText newPwd = new EditText(this);
        newPwd.setHint("new Password");
        final EditText cnfrmPwd = new EditText(this);
        cnfrmPwd.setHint("conform Password");

        // creation of textview
        final TextView textview = new TextView(this);
        textview.setText("Hell");
        textview.setPadding(18, 20, 0, 0);
        textview.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        textview.setVisibility(View.GONE);

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(20, 2, 2, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(oldPwd);
        layout.addView(newPwd);
        layout.addView(cnfrmPwd);
        layout.addView(textview);

        // adjust InputType of edittext.
        oldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        newPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        cnfrmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        String userName = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserName));
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Change Password for \""+userName+"\".");

        // set main layout to alert dialog
        alertDialog.setView(layout);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Change",null);
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        pButton.setTextColor(Color.BLACK);
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getOldText = oldPwd.getText().toString();
                String getNewText = newPwd.getText().toString();
                String getCnfrmText = cnfrmPwd.getText().toString();
            }
        });
    }

    public void onClickForgotPwd(View view) {
        // creation of textview
        String userEmail = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail));
        final TextView textview = new TextView(this);
        textview.setText("Your password will be sent on \""+userEmail+"\" email-id.");
        textview.setTextColor(getResources().getColor(R.color.colorPrimary));
        textview.setTextSize(16);

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(50, 20, 2, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(textview);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setView(layout);
        alertDialog.setTitle("Forgot Password");

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
    }

    public void onClickDeleteAc(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete Account");
        alertDialog.setMessage("Are you sure you want to delete this account?");

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);

        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }
}
