package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomMenuAdapter;
import com.cleanslatetech.floc.models.MenuModel;
import com.cleanslatetech.floc.models.SubMenuModels;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.interfaces.InterfaceRightMenuClick;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        String strActivityId = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdActivityId));
        if(strActivityId == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("rate", 5);
                jsonObject.put("like", 1);
                jsonObject.put("review", 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new SetSharedPreference(this).setString(getResources().getString(R.string.shrdActivityId), String.valueOf(jsonObject));
        }


    }

    @Override
    public void onBackPressed() {
        new SetSharedPreference(BaseAppCompactActivity.this).setString(getResources().getString(R.string.shrdSelectedMenu), null);
        Intent intent = new Intent(this, HomeActivity.class);
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
                    CommonUtilities.customToast(BaseAppCompactActivity.this, "Permission require for registering with Buxa.");
                } else {
                    System.out.println("Permission has been granted by user");
                }
                break;
        }
    }

    public void setToolBar(final String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageView logo = (AppCompatImageView) findViewById(R.id.toolbarAppLogo);
        AppCompatTextView titleToolBar = (AppCompatTextView) findViewById(R.id.toolbarTitle);
        LinearLayout optionText = (LinearLayout) findViewById(R.id.optionsText);

        if (title.equals("Home")) {
            ((AppCompatTextView) findViewById(R.id.onClickHomeOption)).setTextColor(getResources().getColor(R.color.highlight_top_menu));
            ((AppCompatTextView) findViewById(R.id.onClickCreateFloc)).setTextColor(getResources().getColor(R.color.colorPrimary));
            ((AppCompatTextView) findViewById(R.id.onClickEvent)).setTextColor(getResources().getColor(R.color.colorPrimary));

        } else if (title.equals("Build a floc") || title.equals("Build a Platform")) {
            ((AppCompatTextView) findViewById(R.id.onClickCreateFloc)).setText(title);

            ((AppCompatTextView) findViewById(R.id.onClickHomeOption)).setTextColor(getResources().getColor(R.color.colorPrimary));
            ((AppCompatTextView) findViewById(R.id.onClickCreateFloc)).setTextColor(getResources().getColor(R.color.highlight_top_menu));
            ((AppCompatTextView) findViewById(R.id.onClickEvent)).setTextColor(getResources().getColor(R.color.colorPrimary));

        } else if (title.equals("Events")) {
            ((AppCompatTextView) findViewById(R.id.onClickHomeOption)).setTextColor(getResources().getColor(R.color.colorPrimary));
            ((AppCompatTextView) findViewById(R.id.onClickCreateFloc)).setTextColor(getResources().getColor(R.color.colorPrimary));
            ((AppCompatTextView) findViewById(R.id.onClickEvent)).setTextColor(getResources().getColor(R.color.highlight_top_menu));

        }

        findViewById(R.id.onClickHomeOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SetSharedPreference(BaseAppCompactActivity.this).setString(getResources().getString(R.string.shrdSelectedMenu), null);

                if(! title.equals("Home")) {
                    onBackPressed();
                }
            }
        });

        findViewById(R.id.onClickCreateFloc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetSharedPreference(BaseAppCompactActivity.this).setString(getResources().getString(R.string.shrdSelectedMenu), null);
                startActivity(new Intent(BaseAppCompactActivity.this, CreateFlocOptionActivity.class));
            }
        });

        findViewById(R.id.onClickEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SetSharedPreference(BaseAppCompactActivity.this).setString(getResources().getString(R.string.shrdSelectedMenu), null);
                startActivity(new Intent(BaseAppCompactActivity.this, AllEventActivity.class));
            }
        });

        if( title.equals("Home") || title.equals(getResources().getString(R.string.app_name)) ) {
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

    public void createRightPopupMenu() {
        CardView imgviewRightOption = (CardView) findViewById(R.id.rightMenuBar);
        ImageView profileImage = (ImageView) findViewById(R.id.profileImage_toolbar);

        if(profileImage == null) {
            return;
        }

        String loginType = new GetSharedPreference(BaseAppCompactActivity.this)
                .getString(getResources().getString(R.string.shrdLoginType));
        String strMyProfile = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdMyProfile));

        try {
            if(loginType.equals(getResources().getString(R.string.facebookLogin)) ||
                    loginType.equals(getResources().getString(R.string.googleLogin)) ) {
                Glide
                        .with(this)
                        .load(new GetSharedPreference(this).getString("social_profilePic"))
                        .placeholder(R.drawable.textarea_gradient_bg)
                        .into(profileImage);
            }
            else {
                JSONObject joMyProfile = new JSONObject(strMyProfile);
                System.out.println("strMyProfile: "+joMyProfile.getString("ProfilePic").length());

                if( joMyProfile.getString("ProfilePic").equals("null") ) {
                    Glide
                            .with(getApplication())
                            .load( R.drawable.blank_profile )
                            .placeholder(R.drawable.textarea_gradient_bg)
                            .into(profileImage);

                } else {

                    Glide
                            .with(this)
                            .load( CommonVariables.EVENT_IMAGE_SERVER_URL + joMyProfile.getString("ProfilePic"))
                            .placeholder(R.drawable.textarea_gradient_bg)
                            .into(profileImage);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.about_us)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.contact_us)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.f_amp_q)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.terms_amp_conditions)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.privacy_policy)));
        } else {

            arrayMenuModels = new ArrayList<MenuModel>();

            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.recent_flocs)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.archive_floc)));

           /* arraySubMenuModel = new ArrayList<SubMenuModels>();
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.upload_pictures)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.upload_video)));
            arraySubMenuModel.add(new SubMenuModels(getResources().getString(R.string.comment)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.activity), arraySubMenuModel));*/

            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.app_name)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.invite_friend)));
            arrayMenuModels.add(new MenuModel(getResources().getString(R.string.my_profile)));
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
        if( menuName.equals(getResources().getString(R.string.about_us))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.about_us))
                    .putExtra("url", "http://floc.world/Home/About"));

        } else if( menuName.equals(getResources().getString(R.string.contact_us))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.contact_us))
                    .putExtra("url", "http://floc.world/Home/Contact"));

        } else if( menuName.equals(getResources().getString(R.string.f_amp_q))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.f_amp_q))
                    .putExtra("url", "http://floc.world/Home/FAQ"));

        } else if( menuName.equals(getResources().getString(R.string.terms_amp_conditions))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.terms_amp_conditions))
                    .putExtra("url", "http://floc.world/terms.html"));

        } else if( menuName.equals(getResources().getString(R.string.privacy_policy))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.privacy_policy))
                    .putExtra("url", "http://floc.world/privacy-policy.html"));

        } else if( menuName.equals(getResources().getString(R.string.archive_floc))) {

            startActivity(new Intent(getApplicationContext(), ArchiveFlocActivity.class));

        } else if( menuName.equals(getResources().getString(R.string.recent_flocs))) {

            startActivity(new Intent(getApplicationContext(), RecentFlocActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.app_name))) {

            startActivity(new Intent(getApplicationContext(), FlocsActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.invite_friend))) {

            sharedAppLink();

        } else if(menuName.equals(getResources().getString(R.string.my_profile))) {

            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));

        }else if(menuName.equals(getResources().getString(R.string.setting))) {

            startActivity(new Intent(getApplicationContext(), SettingActivity.class));

        } else if(menuName.equals(getResources().getString(R.string.action_logout))) {

            String loginType = new GetSharedPreference(BaseAppCompactActivity.this)
                    .getString(getResources().getString(R.string.shrdLoginType));

            if(loginType.equals(getResources().getString(R.string.appLogin))) {
                // intet for next activity
                handleIntentWhenSignOut(BaseAppCompactActivity.this);
            }
            else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {
                LoginManager.getInstance().logOut();

                // intet for next activity
                handleIntentWhenSignOut(BaseAppCompactActivity.this);
            }
            else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // intet for next activity
                                handleIntentWhenSignOut(BaseAppCompactActivity.this);
                            }
                        });
            }
        }
    }

    private void sharedAppLink() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "FLOCworld. Birds Of UR Feather");
            String sAux = "Download this great FLOCworld application.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.cleanslatetech.floc&hl=en";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            this.startActivity(Intent.createChooser(i, "choose one"));

        } catch(Exception e) {
            //e.toString();
        }
    }
}
