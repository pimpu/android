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
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomMenuAdapter;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;
import com.cleanslatetech.floc.models.MenuModel;
import com.cleanslatetech.floc.models.SubMenuModels;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.InterfaceRightMenuClick;
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
import java.util.Arrays;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

/**
 * Created by pimpu on 2/6/2017.
 */

public class BaseAppCompactActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, InterfaceRightMenuClick {

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

        findViewById(R.id.onClickHomeOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.onClickCreateFloc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseAppCompactActivity.this, CreateFlocActivity.class));
            }
        });

        if(title == null || title.length() <= 0 || title.equals(getResources().getString(R.string.app_name)) ) {
            logo.setVisibility(View.VISIBLE);
//            optionText.setVisibility(View.VISIBLE);
            titleToolBar.setVisibility(View.GONE);
        }
        else {
            logo.setVisibility(View.GONE);
//            optionText.setVisibility(View.GONE);
            titleToolBar.setVisibility(View.VISIBLE);
            titleToolBar.setText(title);
//            toolbar.setBackgroundResource(R.drawable.toolbar_gradient);
        }


        createRightPopupMenu();
        createLeftPopupMenu();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_right_menu_icon, menu);

        MenuItem item = menu.findItem(R.id.idOpenRightMEnu);

        MenuItemCompat.getActionView(item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightPopupWindow.showAsDropDown(findViewById(R.id.appBarRight));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/

    private void createRightPopupMenu() {
        CardView imgviewRightOption = (CardView) findViewById(R.id.rightMenuBar);

        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(this);
        // initialize a pop up window type
        popupWindowView(popupWindow,"RightMenu");

        imgviewRightOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(findViewById(R.id.appBarRight));
            }
        });
    }

    private void createLeftPopupMenu() {
        AppCompatImageView imgviewLeftOption = (AppCompatImageView) findViewById(R.id.leftMenuBar);
        // initialize a pop up window type
        final PopupWindow popupWindow = new PopupWindow(this);

        popupWindowView(popupWindow,"LeftMenu");

        imgviewLeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(findViewById(R.id.appBarLeft));
            }
        });
    }

    /**
     * show popup window method reuturn PopupWindow
     * @param popupWindow
     */
    private void popupWindowView(final PopupWindow popupWindow, String menuSide) {
        ArrayList<MenuModel> arrayMenuModels;
        ArrayList<SubMenuModels> arraySubMenuModel;

        if(menuSide.equals("LeftMenu")) {
            arrayMenuModels = new ArrayList<MenuModel>();

//            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.home)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.about_us)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.contact_us)));
//            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.create_floc)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.f_amp_q)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.terms_amp_conditions)));
        } else {

            arrayMenuModels = new ArrayList<MenuModel>();

            /*arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.experiential)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.professional)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.personal_floc)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.recent_flocs), arraySubMenuModel));*/
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.recent_flocs)));

            arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.upload_pictures)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.upload_video)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.comment)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.activity), arraySubMenuModel));

            /*arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.running_floc)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.completed_floc)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.pause_floc)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.cancelled_floc)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.invite_users_to_event)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.request_to_join)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.app_name), arraySubMenuModel));*/
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.app_name)));

            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.invite_friend)));

            /*arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.personal_profile)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.financial)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.my_profile), arraySubMenuModel));*/
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.my_profile)));

            /*arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.change_password)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.forgot_password)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.delete_account)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.setting), arraySubMenuModel)); */
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.setting)));

            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.action_logout)));

        }

        // the drop down list is a list view
        final ListView listViewSort = new ListView(this);
        CustomMenuAdapter adapter = new CustomMenuAdapter(arrayMenuModels, BaseAppCompactActivity.this,  popupWindow, findViewById(R.id.appBarRight));

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);
        listViewSort.setBackgroundColor(getResources().getColor(R.color.white));

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(600);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);
    }

    @Override
    public void getSubmenuClick(String menuName) {
        if( menuName.equals(getResources().getString(R.string.home))) {

            onBackPressed();

        } /*else if( menuName.equals(getResources().getString(R.string.create_floc))) {

            startActivity(new Intent(BaseAppCompactActivity.this, CreateFlocActivity.class));

        }*/ else if( menuName.equals(getResources().getString(R.string.recent_flocs))) {

            startActivity(new Intent(getApplicationContext(), RecentFlocActivity.class));

        } /*else if( menuName.equals(getResources().getString(R.string.experiential))) {

            startActivity(new Intent(getApplicationContext(), RecentFlocExperientalActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.professional))) {

            startActivity(new Intent(getApplicationContext(), RecentFlocProfessionalActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.personal_floc))) {

            startActivity(new Intent(getApplicationContext(), RecentFlocPersonalActivity.class));

        }*/ else if(menuName.equals(getResources().getString(R.string.upload_pictures))){

        } else if(menuName.equals(getResources().getString(R.string.upload_video))) {

        } else if(menuName.equals(getResources().getString(R.string.comment))) {

        } /*else if(menuName.equals(getResources().getString(R.string.running_floc))) {

            startActivity(new Intent(getApplicationContext(), FlocRunningActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.completed_floc))) {

            startActivity(new Intent(getApplicationContext(), FlocCompletedActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.pause_floc))) {

            startActivity(new Intent(getApplicationContext(), FlocPauseActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.cancelled_floc))) {

            startActivity(new Intent(getApplicationContext(), FlocCancelActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.invite_users_to_event))) {

            openInviteUserToFlocDialog();

        } else if(menuName.equals(getResources().getString(R.string.request_to_join))) {

            startActivity(new Intent(getApplicationContext(), FlocRequestActivity.class));

        }*/ else if(menuName.equals(getResources().getString(R.string.app_name))) {

            startActivity(new Intent(getApplicationContext(), FlocsActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.invite_friend))) {

            sharedAppLink();

        } else if(menuName.equals(getResources().getString(R.string.my_profile))) {

            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));

        }/* else if(menuName.equals(getResources().getString(R.string.personal_profile))) {

            startActivity(new Intent(getApplicationContext(), PersonalProfileActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.financial))) {

            startActivity(new Intent(getApplicationContext(), PersonalFinanceActivity.class));

        }*/ else if(menuName.equals(getResources().getString(R.string.setting))) {

            startActivity(new Intent(getApplicationContext(), SettingActivity.class));

        } /*else if(menuName.equals(getResources().getString(R.string.change_password))) {

            openChangePwdDialog();

        } else if(menuName.equals(getResources().getString(R.string.forgot_password))) {

            openForgotPwdDialog();

        } else if(menuName.equals(getResources().getString(R.string.delete_account))) {

            openDeleteAcDialog();

        }*/ else if(menuName.equals(getResources().getString(R.string.action_logout))) {

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
        }
    }

    private void sharedAppLink() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Floc");
            String sAux = " Download this great Floc application.\n\n";
            sAux = sAux + "here floc play store link will come\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            this.startActivity(Intent.createChooser(i, "choose one"));

        } catch(Exception e) {
            //e.toString();
        }
    }
}
