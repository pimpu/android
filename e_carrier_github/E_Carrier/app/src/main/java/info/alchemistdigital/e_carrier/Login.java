package info.alchemistdigital.e_carrier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.alchemistdigital.e_carrier.activity.DriverViewActivity;
import info.alchemistdigital.e_carrier.activity.MainActivity;
import info.alchemistdigital.e_carrier.asynctask.ForgotPassword;
import info.alchemistdigital.e_carrier.asynctask.LoginAsyncTask;
import info.alchemistdigital.e_carrier.services.GetLatLongService;
import info.alchemistdigital.e_carrier.utilities.AlertDialogManager;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.Validation;

public class Login extends Fragment{
    EditText txtLoginEmail,txtLoginPwd;
    TextInputLayout emailInputLayout,pwdInputLayout;
    private SharedPreferences sharedPreferenceLogin;
    private String loginSharedPrefEmail;
    private TextView tvForgotPwd;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    public Login() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.login, container, false);

        txtLoginEmail    = (EditText) rootView.findViewById(R.id.login_email);
        txtLoginPwd      = (EditText) rootView.findViewById(R.id.login_password);
        emailInputLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        pwdInputLayout   = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
//        tvForgotPwd      = (TextView) rootView.findViewById(R.id.idForgotPwd);

        // made sharedprefrence
        sharedPreferenceLogin=getActivity().getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);

        // take values for validation purpose.
        // it validates entered email id and pwd when Login button pressed
        loginSharedPrefEmail = sharedPreferenceLogin.getString(getResources().getString(R.string.loginEmail),"");
        txtLoginEmail.setText(loginSharedPrefEmail);

        final RippleView rippleView = (RippleView)rootView.findViewById(R.id.ripplebtn_login);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                String email = txtLoginEmail.getText().toString();
                String pwd = txtLoginPwd.getText().toString();
                Boolean EmailValid = Validation.emailValidate(email);
                Boolean PWDValid = Validation.isEmptyString(pwd);

                if (!EmailValid) {
                    emailInputLayout.setErrorEnabled(true);
                    emailInputLayout.setError("You need to enter correct email-id");
                } else {
                    emailInputLayout.setErrorEnabled(false);
                }

                if (!PWDValid) {
                    pwdInputLayout.setErrorEnabled(true);
                    pwdInputLayout.setError("password field is empty");
                } else {
                    pwdInputLayout.setErrorEnabled(false);
                }

                if (EmailValid && PWDValid) {

                    cd = new ConnectionDetector(AuthenticationScreen.getInstance());

                    // Check if Internet present
                    if (!cd.isConnectingToInternet()) {
                        // Internet Connection is not present
                        alert.showAlertDialog(AuthenticationScreen.getInstance(),
                                "Internet Connection Error",
                                "Please connect to working Internet connection", false);
                        // stop executing code by return
                        return;
                    } else {

                        new extendLoginAsyncTask(AuthenticationScreen.getInstance(), email, pwd).execute();
                    }
                }
            }
        });

        /*tvForgotPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Forgot Password");
                builder.setMessage("password will be send on \"" + loginSharedPrefEmail + "\" email.");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        int loginId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);
                        new extendsForgotPasswordAsync(loginSharedPrefEmail,String.valueOf(loginId)).execute();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });*/
        return rootView;
    }

    private class extendLoginAsyncTask extends LoginAsyncTask {
        AuthenticationScreen context;
        String email;
        public extendLoginAsyncTask(AuthenticationScreen instance, String email, String pwd) {
            super(instance,email,pwd);
            context = instance;
            this.email=email;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            // check for success tag
            try {
                Log.d("Create Login Response", result.toString());

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if (success == 1) {

                    JSONArray userDataArray = json.getJSONArray(CommonUtilities.TAG_MESSAGE);
                    // get first product object from JSON Array
                    JSONObject  userDataObject= userDataArray.getJSONObject(0);


                    SharedPreferences sharedPreferenceLogin= context.getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    SharedPreferences.Editor editor = sharedPreferenceLogin.edit();

                    // it store the Register true value of user for purpose of user is registered with this app.
                    editor.putString(context.getResources().getString(R.string.boolean_login_sharedPref), "true");
                    editor.putInt(context.getResources().getString(R.string.loginId), userDataObject.getInt("loggedId"));
                    editor.putString(context.getResources().getString(R.string.loginName), userDataObject.getString("name"));
                    editor.putString(context.getResources().getString(R.string.loginEmail), email);
                    editor.putString(context.getResources().getString(R.string.userType), userDataObject.getString("who"));
                    editor.commit();

                    if( userDataObject.getString("who").equals("customer") )
                    {
                        Intent intent = new Intent(context,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.finish();
                        startActivity(intent);
                    }
                    else {
                        // start location service
                        getActivity().startService(new Intent(getActivity(), GetLatLongService.class));
                        
                        Intent intent = new Intent(context,DriverViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.finish();
                        startActivity(intent);
                    }
                } // if (success == 1)
                else if (success == 0 || success == 2){
                    String message = json.getString(CommonUtilities.TAG_MESSAGE);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class extendsForgotPasswordAsync extends ForgotPassword {
        public extendsForgotPasswordAsync(String loginEmail, String loginId) {
            super(getActivity(), loginEmail, loginId);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                System.out.println("forgot password request: " + result.toString());

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);
                String message = json.getString(CommonUtilities.TAG_MESSAGE);
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
