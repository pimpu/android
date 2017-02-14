package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomMenuAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

/**
 * Created by pimpu on 2/6/2017.
 */

public class BaseAppCompactActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        // google setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if(!hasPermissions(this, CommonVariables.PERMISSIONS)){
            ActivityCompat.requestPermissions(this, CommonVariables.PERMISSIONS, CommonVariables.REQUEST_PERMISSION);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonVariables.REQUEST_PERMISSION:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission has been denied by user");
                    Toast.makeText(getApplicationContext(), "Permission require for registering with Buxa.",Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Permission has been granted by user");
                }
                break;
        }
    }

    public void setToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageView logo = (AppCompatImageView) findViewById(R.id.toolbarAppLogo);
        AppCompatTextView titleToolBar = (AppCompatTextView) findViewById(R.id.toolbarTitle);
        LinearLayout optionText = (LinearLayout) findViewById(R.id.optionsText);

        if(title == null || title.length() <= 0 ) {
            logo.setVisibility(View.VISIBLE);
            optionText.setVisibility(View.VISIBLE);
            titleToolBar.setVisibility(View.GONE);
        }
        else {
            logo.setVisibility(View.GONE);
            optionText.setVisibility(View.GONE);
            titleToolBar.setVisibility(View.VISIBLE);
            titleToolBar.setText(title);
            toolbar.setBackgroundResource(R.drawable.toolbar_gradient);
        }

        createLeftPopupMenu();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_right_menu_icon, menu);

        MenuItem item = menu.findItem(R.id.idOpenRightMEnu);
        MenuItemCompat.getActionView(item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRightMenuPopup(v);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void createLeftPopupMenu() {
        final Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        AppCompatImageView imgviewLeftOption = (AppCompatImageView) findViewById(R.id.leftMenuBar);

        //Creating the instance of PopupMenu
        final PopupMenu popup = new PopupMenu(wrapper, (LinearLayout)findViewById(R.id.appBarLeft) );
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.left_menu, popup.getMenu());

        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(this);

        popupWindowsort(popupWindow);

        imgviewLeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.showAsDropDown(findViewById(R.id.appBarLeft));
//                popup.show();
            }
        });



        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                item.setChecked(!item.isChecked());
                /*if(item.isChecked()) {
                }*/

                Toast.makeText(
                        BaseAppCompactActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();

                invalidateOptionsMenu();
                return true;
            }
        });
    }

    /**
     * show popup window method reuturn PopupWindow
     * @param popupWindow
     */
    private void popupWindowsort(final PopupWindow popupWindow) {


        final ArrayList<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Apple Pie"));
        dataModels.add(new DataModel("Banana Bread"));
        dataModels.add(new DataModel("Cupcake"));
        dataModels.add(new DataModel("Donut"));
        dataModels.add(new DataModel("Eclair"));

        final ArrayAdapter<String> adapter = new CustomMenuAdapter(dataModels, getApplicationContext(),  popupWindow);
        // the drop down list is a list view
        final ListView listViewSort = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);
        listViewSort.setBackgroundColor(getResources().getColor(R.color.white));

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(500);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);
    }

    public class DataModel {

        String name;

        public DataModel(String name ) {
            this.name=name;
        }

        public String getName() {
            return name;
        }

    }

    public void createRightMenuPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, (LinearLayout)findViewById(R.id.appBarRight));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.right_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case  R.id.experiential:
                        startActivity(new Intent(getApplicationContext(), RecentFlocExperientalActivity.class));
                        break;

                    case R.id.professional:
                        startActivity(new Intent(getApplicationContext(), RecentFlocProfessionalActivity.class));
                        break;

                    case  R.id.personal:
                        startActivity(new Intent(getApplicationContext(), RecentFlocPersonalActivity.class));
                        break;

                    case R.id.uploadPicture:
                        break;

                    case  R.id.uploadVideo:
                        break;

                    case  R.id.comment:
                        break;

                    case R.id.runningFloc:
                        break;

                    case  R.id.completedFloc:
                        break;

                    case  R.id.pauseFloc:
                        break;

                    case  R.id.cancelledFloc:
                        break;

                    case  R.id.inviteUser:
                        break;

                    case  R.id.requestToJoin:
                        break;

                    case  R.id.inviteFriend:
                        break;

                    case  R.id.profilePersonal:
                        startActivity(new Intent(getApplicationContext(), PersonalProfileActivity.class));
                        break;

                    case  R.id.financial:
                        startActivity(new Intent(getApplicationContext(), PersonalFinanceActivity.class));
                        break;

                    /*case  R.id.profilePic:
                        break;*/

                    case  R.id.changePwd:
                        openChangePwdDialog();
                        break;

                    case  R.id.forgotPwd:
                        openForgotPwdDialog();
                        break;

                    case  R.id.deleteAccount:
                        openDeleteAcDialog();
                        break;

                    case  R.id.logout:
                        String loginType = new GetSharedPreference(BaseAppCompactActivity.this)
                                .getString(getResources().getString(R.string.shrdLoginType));

                        if(loginType.equals(getResources().getString(R.string.appLogin))) {
                            // intet for next activity
                            handleIntentWhenSignOut(BaseAppCompactActivity.this, false);
                        }
                        else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {
                            LoginManager.getInstance().logOut();

                            // intet for next activity
                            handleIntentWhenSignOut(BaseAppCompactActivity.this, false);
                        }
                        else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            // intet for next activity
                                            handleIntentWhenSignOut(BaseAppCompactActivity.this, false);
                                        }
                                    });
                        }
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void openChangePwdDialog() {
        Context wrapper = new ContextThemeWrapper(this, R.style.AppBaseTheme);

        final EditText oldPwd = new EditText(wrapper);
        oldPwd.setHint("old Password");
        final EditText newPwd = new EditText(wrapper);
        newPwd.setHint("new Password");
        final EditText cnfrmPwd = new EditText(wrapper);
        cnfrmPwd.setHint("conform Password");

        // creation of textview
        final TextView textview = new TextView(wrapper);
        textview.setText("Hell");
        textview.setPadding(18, 20, 0, 0);
        textview.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        textview.setVisibility(View.GONE);

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(wrapper);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(wrapper);

        String userName = new GetSharedPreference(wrapper).getString(getResources().getString(R.string.shrdUserName));
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

    private void openForgotPwdDialog() {
        Context wrapper = new ContextThemeWrapper(this, R.style.AppBaseTheme);

        // creation of textview
        String userEmail = new GetSharedPreference(wrapper).getString(getResources().getString(R.string.shrdUserEmail));
        final TextView textview = new TextView(wrapper);
        textview.setText("Your password will be sent on \""+userEmail+"\" email-id.");
        textview.setTextColor(getResources().getColor(R.color.colorPrimary));
        textview.setTextSize(16);

        //  alert dialog main layout
        LinearLayout layout = new LinearLayout(wrapper);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(50, 20, 2, 2);

        // adding edittext and textview to alert dialog main layout
        layout.addView(textview);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(wrapper);

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

    private void openDeleteAcDialog() {
        Context wrapper = new ContextThemeWrapper(this, R.style.AppBaseTheme);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(wrapper);

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
