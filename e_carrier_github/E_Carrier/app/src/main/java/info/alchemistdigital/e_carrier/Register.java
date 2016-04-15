package info.alchemistdigital.e_carrier;

/**
 * Created by Tapan on 11-Oct-15.
 */

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.alchemistdigital.e_carrier.activity.MainActivity;
import info.alchemistdigital.e_carrier.adapter.EmailListAdapter;
import info.alchemistdigital.e_carrier.asynctask.UserInfoInsertAsyncTask;
import info.alchemistdigital.e_carrier.model.Email_Account_Item;
import info.alchemistdigital.e_carrier.utilities.AlertDialogManager;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.CreatingShortCut;

import static info.alchemistdigital.e_carrier.utilities.Validation.isEmptyString;
import static info.alchemistdigital.e_carrier.utilities.Validation.phoneValiate;

public class Register extends Fragment {
    EditText txtregisterPwd,txtRegisterRetypePwd,getTxtRegisterPhoneNo,txtRegisterName;
    TextView txtRegisterEmail;
    TextInputLayout pwdInputLayoutReg,retypeInputLayoutReg,phoneInputLayoutReg,nameInputLayoutReg;

    private ArrayList<Email_Account_Item> list = null;
    private ListView listView;
    private EmailListAdapter listadaptor;
    private static final int PICK_ACCOUNT_REQUEST = 0;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.register, container, false);

        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String mobileNo = tm.getLine1Number();

        txtRegisterEmail      = (TextView) rootView.findViewById(R.id.email_reg);
        txtregisterPwd        = (EditText) rootView.findViewById(R.id.password_reg);
        txtRegisterRetypePwd  = (EditText) rootView.findViewById(R.id.password_reg2);
        getTxtRegisterPhoneNo = (EditText) rootView.findViewById(R.id.number_reg);
        txtRegisterName       = (EditText) rootView.findViewById(R.id.name_reg);

        phoneInputLayoutReg  = (TextInputLayout) rootView.findViewById(R.id.input_layout_number_reg);
        pwdInputLayoutReg    = (TextInputLayout) rootView.findViewById(R.id.input_layout_password_reg);
        retypeInputLayoutReg = (TextInputLayout) rootView.findViewById(R.id.input_layout_password_reg2);
        nameInputLayoutReg   = (TextInputLayout) rootView.findViewById(R.id.input_layout_userName);

        // get emails addresses which are registered on mobile device
        list = CommonUtilities.getEmailsData(getActivity());

        if( list.size() == 1 ){
            txtRegisterEmail.setText( list.get(0).getName() );
        }

        txtRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list.size() > 0) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setCancelable(true);
                    View inflaterOfEmails = getActivity().getLayoutInflater().inflate(R.layout.email_name_listview, null);
                    dialogBuilder.setView(inflaterOfEmails);
                    AlertDialog alertDialog = dialogBuilder.create();

                    listView = (ListView) inflaterOfEmails.findViewById(R.id.idEmailName);
                    listadaptor = new EmailListAdapter(getActivity(), R.layout.email_account_name_view, list, txtRegisterEmail, alertDialog);
                    listView.setAdapter(listadaptor);

                    alertDialog.show();
                } else {
                    Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
                    startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
                }
            }
        });

        if( mobileNo != null && !mobileNo.isEmpty()){
            getTxtRegisterPhoneNo.setText(mobileNo);
        }


        final RippleView rippleView = (RippleView)rootView.findViewById(R.id.ripplebtn_Register);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                String regName  = txtRegisterName.getText().toString();
                String regEmail = txtRegisterEmail.getText().toString();
                String regPwd   = txtregisterPwd.getText().toString();
                String regRePwd = txtRegisterRetypePwd.getText().toString();
                String regPhone = getTxtRegisterPhoneNo.getText().toString();

                Boolean NameValid  = isEmptyString(regName);
                Boolean EmailValid = isEmptyString(regEmail);
                Boolean PWDValid   = isEmptyString(regPwd);
                Boolean RePWDValid = isEmptyString(regRePwd);
                Boolean PhoneValid = phoneValiate(regPhone);

                if (!NameValid) {
                    nameInputLayoutReg.setErrorEnabled(true);
                    nameInputLayoutReg.setError("name field is incorrect");
                } else {
                    nameInputLayoutReg.setErrorEnabled(false);
                }

                if (!EmailValid) {
                    Toast.makeText(getActivity(),"Email-Id is not select.",Toast.LENGTH_LONG).show();
                }


                if (!PhoneValid) {
                    phoneInputLayoutReg.setErrorEnabled(true);
                    phoneInputLayoutReg.setError("phone field is incorrect");
                } else {
                    phoneInputLayoutReg.setErrorEnabled(false);
                }

                if (!PWDValid) {
                    pwdInputLayoutReg.setErrorEnabled(true);
                    pwdInputLayoutReg.setError("password field is empty");
                } else {
                    pwdInputLayoutReg.setErrorEnabled(false);
                }

                if (!RePWDValid) {
                    retypeInputLayoutReg.setErrorEnabled(true);
                    retypeInputLayoutReg.setError("ReType password field is empty");
                } else if (!regPwd.equals(regRePwd)) {
                    retypeInputLayoutReg.setError("password dosen't match");
                    RePWDValid = false;
                } else {
                    retypeInputLayoutReg.setErrorEnabled(false);
                }

                if( EmailValid && PhoneValid && PWDValid && RePWDValid && NameValid){

                    cd = new ConnectionDetector(getActivity().getApplicationContext());

                    // Check if Internet present
                    if (!cd.isConnectingToInternet()) {
                        // Internet Connection is not present
                        alert.showAlertDialog(AuthenticationScreen.getInstance(),
                                "Internet Connection Error",
                                "Please connect to working Internet connection", false);
                        // stop executing code by return
                        return;
                    }
                    else {
                        new extendUserInfoInsertAsyncTask(AuthenticationScreen.getInstance(),regName,regEmail,regPhone,regPwd).execute();
                    }

                }

            }
        });

        return  rootView;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == getActivity().RESULT_OK) {
            txtRegisterEmail.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
        }
    }

    private class extendUserInfoInsertAsyncTask extends UserInfoInsertAsyncTask {
        AuthenticationScreen context;
        String name,email;
        public extendUserInfoInsertAsyncTask(AuthenticationScreen instance, String regName, String regEmail, String regPhone, String regPwd) {
            super(instance,regName,regEmail,regPhone,regPwd);

            context = instance;
            name = regName;
            email = regEmail;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            // check for success tag
            try {
                Log.d("Create Response", result.toString());

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                String message = json.getString(CommonUtilities.TAG_MESSAGE);

                if (success == 1) {

                    int id = json.getInt(CommonUtilities.TAG_MESSAGE);

                    SharedPreferences sharedPreferenceLogin= this.context.getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    SharedPreferences.Editor editor = sharedPreferenceLogin.edit();

                    // it store the Register true value of user for purpose of user is registered with this app.
                    editor.putString(context.getResources().getString(R.string.boolean_login_sharedPref), "true");
                    editor.putInt(context.getResources().getString(R.string.loginId), id);
                    editor.putString(context.getResources().getString(R.string.loginName), name);
                    editor.putString(context.getResources().getString(R.string.loginEmail), email);
                    editor.putString(context.getResources().getString(R.string.userType), "customer");
                    editor.commit();

                    // create shortcut when user Register with this app.
                    CreatingShortCut.addShortcut(context);

                    Intent intent = new Intent(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.finish();
                    startActivity(intent);

                } // if (success == 1)
                else if (success == 2 || success == 3){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
